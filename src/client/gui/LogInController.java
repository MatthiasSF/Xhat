package client.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.ClientCommunications;
import client.ClientLogger;
import common.ResultCode;

public class LogInController {
	private JFrame frameLogIn;
	private JFrame frameRegister;
	private ClientCommunications clientCommunications;
	private LogInPanel logInPanel;
	private RegisterNewUserPanel registerNewUserPanel;

	public LogInController(ClientCommunications clientCommunications, String userName, String password) {
		this.clientCommunications = clientCommunications;
		showLogInPanel(userName, password);
	}
	
	public void showLogInPanel(String userName, String password) {
		disposeFrameRegister();
		logInPanel = new LogInPanel(this, userName, password);
		frameLogIn = new JFrame("Sign in");
		frameLogIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameLogIn.setResizable(false);
		frameLogIn.add(logInPanel);
		frameLogIn.pack();
		frameLogIn.setLocationRelativeTo(null);
		frameLogIn.setVisible(true);
	}

	public void disposeLogInPanel() {
		if (frameLogIn != null) {
			frameLogIn.dispose();
		}
	}
	
	public void showRegisterNewUserPanel() {
		disposeLogInPanel();
		registerNewUserPanel = new RegisterNewUserPanel(this);
		frameRegister = new JFrame("Register new user");
		frameRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //handle operation in windowClosing() method of a registered WindowListener object. 
		frameRegister.addWindowListener(null); //TODO Create WindowListener
		frameRegister.setResizable(false);
		frameRegister.add(registerNewUserPanel);
		frameRegister.pack();
		frameRegister.setLocationRelativeTo(null);
		frameRegister.setVisible(true);
	}
	
	public void disposeFrameRegister() {
		if (frameRegister != null) {
			frameRegister.dispose();
		}
	}

	public void login(String userName, String password) {
		int result = clientCommunications.login(userName, password);
		if (result == ResultCode.ok) { //Success
			ClientLogger.logInfo("User successfully logged in: " + userName);
			disposeLogInPanel();
		} else {
			String errorMsg;
			if (result == ResultCode.wrongCredentials) {
				errorMsg = "Log in failed: Wrong username or password.";
			} else if(result == ResultCode.serverDown) {
				errorMsg = "Log in failed: The communication with the server failed.";
			} else if (result == ResultCode.wrongUserNameAndPasswordFormat) {
				errorMsg = "Log in failed: Username and Password has wrong format.";
			} else if(result == ResultCode.wrongUsernameFormat) {
				errorMsg = "Log in failed: Username has wrong format.";
			} else if(result == ResultCode.wrongPasswordFormat) {
				errorMsg = "Log in failed: Password has wrong format.";
			} else {
				errorMsg = "Log in failed.";
			}
			ClientLogger.logError(errorMsg);
			JOptionPane.showMessageDialog(null, errorMsg, "Failure", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}

	public void register(String userName, String password, String passwordRepeat) {
		if (password.equals(passwordRepeat)) {
			int result = clientCommunications.register(userName, password);
			if (result == ResultCode.ok) {
				ClientLogger.logInfo("New user successfully registered: " + userName);
				JOptionPane.showMessageDialog(null, "New user successfully registered: " + userName, "Info", JOptionPane.INFORMATION_MESSAGE);
				showLogInPanel(userName, "");
			} else {
				String errorMsg;
				if (result == ResultCode.wrongUserNameAndPasswordFormat) {
					errorMsg = "Registration failed: Username and password not accepted.";
				} else if (result == ResultCode.wrongUsernameFormat) {
					errorMsg = "Registration failed: Username has wrong format.";
				} else if (result == ResultCode.wrongPasswordFormat) {
					errorMsg = "Registration failed: Password has wrong format.";
				} else if (result == ResultCode.userNameAlreadyTaken) {
					errorMsg = "Registration failed: Username already taken.";
				} else if(result == ResultCode.serverDown) {
				    errorMsg = "Registration failed: Could not connect to server."; //Krav 01 
				} else {
					errorMsg = "Registration failed.";
				}
				ClientLogger.logError(errorMsg);
				JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Passwords does not match", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean hasSpecialCharacters(String string) {
		for(Character c : string.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return true;
			}
		}
		return false;
	}
}
