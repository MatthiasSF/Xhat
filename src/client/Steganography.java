/*
 * Credits to William_Wilson for stego-code
 * http://www.dreamincode.net/forums/topic/27950-steganography/
 */
package client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Klassen hanterar kryptering samt dekryptering med steganografi.
 */
public final class Steganography {
	
	/**
	 * Krypterar <code>payload</code> med steganografi i en slumpmässigt genererad bild-fil.
	 * @param payload Den data som ska krypteras.
	 * @return En slumpmässigt genererad bild-fil innehållandes <code>payload</code> krypterat med steganografi.
	 */
	public BufferedImage encode(byte[] payload) throws Exception {
		BufferedImage image_orig = generateRandomImage();

		// user space is not necessary for Encrypting
		BufferedImage image = user_space(image_orig);
		image = add_text(image, payload);

		return image;
	}
	
	/**
	 * Dekrypterar <code>stegoFile</code> med steganografi.
	 * @param stegoImage En bild-fil innehållandes en payload krypterat med steganografi.
	 * @return En byte-array innehållandes den avkrypterade payloaden.
	 */
	public byte[] decode(BufferedImage stegoImage) {
		byte[] decode = null;
		try {
			// user space is necessary for decrypting
			BufferedImage image = user_space(stegoImage);
			decode = decode_text(get_byte_data(image));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There is no hidden message in this image!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return decode;
	}

	/**
	 * Genererar abstrakt konst.
	 * 
	 * @return En BufferedImage föreställandes abstrakt konst.
	 */
	private BufferedImage generateRandomImage() {
		BufferedImage bufferedImage = new BufferedImage(350, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		AbstractArt drawer = new AbstractArt(g2, bufferedImage.getWidth(), bufferedImage.getHeight());
		drawer.drawRandomColorBackground();
		// drawer.drawColorNoiseBackground();
		drawer.drawRandomLines(250);
		g2.dispose();
		return bufferedImage;
	}

	/**
	 *Handles the addition of text into an image
	 *@param carrier The image to add hidden text to
	 *@param payload	 The text to hide in the image
	 *@return Returns the image with the text embedded in it
	 */
	private BufferedImage add_text(BufferedImage carrier, byte[] payload) throws Exception {
		// convert all items to byte arrays: image, message, message length
		byte img[] = get_byte_data(carrier);
		byte msg[] = payload;
		byte len[] = bit_conversion(msg.length);
		encode_text(img, len, 0); // 0 first positiong
		encode_text(img, msg, 32); // 4 bytes of space for length: 4bytes*8bit = 32 bits
		return carrier;
	}

	/**
	 *Creates a user space version of a Buffered Image, for editing and saving bytes
	 *@param image The image to put into user space, removes compression interferences
	 *@return The user space version of the supplied image
	 */
	private BufferedImage user_space(BufferedImage image) {
		// create new_img with the attributes of image
		BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); // release all allocated memory for this image
		return new_img;
	}

	/**
	 *Gets the byte array of an image
	 *@param image The image to get byte data from
	 *@return Returns the byte array of the image supplied
	 *@see Raster
	 *@see WritableRaster
	 *@see DataBufferByte
	 */
	private byte[] get_byte_data(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}
	
	public static byte[] imageToByteArray(BufferedImage image) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "PNG", baos);
			bytes = baos.toByteArray();
			baos.close();
		} catch (IOException e) {}
		return bytes;
	}
	
	public static BufferedImage byteArrayToImage(byte[] bytes) {
		BufferedImage decodedPayloadImage = null;
		if(bytes != null) {
			try {
				decodedPayloadImage = ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {}
		}
		return decodedPayloadImage;
	}

	/**
	 *Gernerates proper byte format of an integer
	 *@param i The integer to convert
	 *@return Returns a byte[4] array converting the supplied integer into bytes
	 */
	private byte[] bit_conversion(int i) {
		// originally integers (ints) cast into bytes
		// byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
		// byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
		// byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
		// byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);
	
		// only using 4 bytes
		byte byte3 = (byte) ((i & 0xFF000000) >>> 24); // 0
		byte byte2 = (byte) ((i & 0x00FF0000) >>> 16); // 0
		byte byte1 = (byte) ((i & 0x0000FF00) >>> 8); // 0
		byte byte0 = (byte) ((i & 0x000000FF));
		// {0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
		return (new byte[] { byte3, byte2, byte1, byte0 });
	}
	
	/**
	 *Encode an array of bytes into another array of bytes at a supplied offset
	 *@param carrier	 Array of data representing an image
	 *@param addition Array of data to add to the supplied image data array
	 *@param offset	  The offset into the image array to add the addition data
	 *@return Returns data Array of merged image and addition data
	 */
	private byte[] encode_text(byte[] carrier, byte[] addition, int offset) {
		//check that the data + offset will fit in the image
		if(addition.length + offset > carrier.length) {
			throw new IllegalArgumentException("Carrier not long enough!");
		}
		//loop through each addition byte
		for(int i=0; i<addition.length; ++i) {
			//loop through the 8 bits of each byte
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset) { //ensure the new offset value carries on through both loops
				//assign an integer to b, shifted by bit spaces AND 1
				//a single bit of the current byte
				int b = (add >>> bit) & 1;
				//assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
				//changes the last bit of the byte in the image to be the bit of addition
				carrier[offset] = (byte)((carrier[offset] & 0xFE) | b );
			}
		}
		return carrier;
	}
	
	/**
	 *Retrieves hidden text from an image
	 *@param image Array of data, representing an image
	 *@return Array of data which contains the hidden text
	 */
	private byte[] decode_text(byte[] image) {
		int length = 0;
		int offset = 32;
		// loop through 32 bytes of data to determine text length
		for (int i = 0; i < 32; ++i) { // i=24 will also work, as only the 4th byte contains real data
			length = (length << 1) | (image[i] & 1);
		}

		byte[] result = new byte[length];

		// loop through each byte of text
		for (int b = 0; b < result.length; ++b) {
			// loop through each bit within a byte of text
			for (int i = 0; i < 8; ++i, ++offset) {
				// assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}

	//Main-metod enbart för test
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Steganography stego = new Steganography();
				BufferedImage payloadImage;
				try {
					payloadImage = ImageIO.read(new File("test.jpg"));
//					byte[] payloadData = stego.get_byte_data(payloadImage);
					byte[] payloadData = imageToByteArray(payloadImage);
					
//					payloadData = "testhejhej hopp!".getBytes();
					
					BufferedImage encodedStegoData = stego.encode(payloadData);
					byte[] decodedPayloadData = stego.decode(encodedStegoData);
//					System.out.println(new String(decodedPayloadData));
					if(decodedPayloadData != null) {
						BufferedImage decodedPayloadImage = byteArrayToImage(decodedPayloadData);
						if(decodedPayloadImage != null) {
							JFrame frame = new JFrame("IconWindow");
							// frame.getContentPane().setBackground(Color.WHITE);
							frame.setResizable(false);
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							frame.add(new JLabel(new ImageIcon(decodedPayloadImage)));
							frame.pack();
							frame.setLocationRelativeTo(null);
							frame.setVisible(true);
						} else {
							System.out.println("BufferedImageData could not be decoded");
						}
					} else {
						System.out.println("decode error");
					}
				} catch (Exception e) {
					System.out.println("Could not load test.jpg");
				}
			}
		});
	}
}
