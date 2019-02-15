package client;

import client.gui.LogInController;
import common.ConfigProperties;

public class InitClient {

	public static void main(String[] args) {
		String ip = ConfigProperties.getClientProperty("ip");
		int port = Integer.parseInt(ConfigProperties.getClientProperty("port"));
		Data data = new Data();
		ClientCommunications clientCommunications = new ClientCommunications(ip, port, data);
		LogInController logInController = new LogInController(clientCommunications, "Test1", "password");
	}
}
