package client;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class ClientLogger {
	private final static Logger clientErrorLog = Logger.getLogger("clientErrorLog");
	private static FileHandler errorFile = null;
	
	
	public static void logInfo(String info) {
		System.out.println(info);
	}
	
	public static void logCommunication(String com) {
		System.out.println(com);
	}

	public static void logError(String error) {
		try {
			new File("client_logs").mkdir();
			errorFile = new FileHandler("client_logs/myapp-log.%u.%g.txt");
			errorFile.setFormatter(new SimpleFormatter());
			clientErrorLog.addHandler(errorFile);
			System.out.println(errorFile);
			System.err.println(error);
			clientErrorLog.severe(error);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
