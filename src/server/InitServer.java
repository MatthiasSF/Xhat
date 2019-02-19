package server;

import common.ConfigProperties;
import sun.security.krb5.Config;

public class InitServer {

	public static void main(String[] args) {
		ThreadPool threadPool = new ThreadPool(30);
		threadPool.start();
//		ServerLogger serverLogger = new ServerLogger();
		int port = Integer.parseInt(ConfigProperties.getServerProperty("port"));
		ClientsManager clientsManager = new ClientsManager(threadPool);
		ServerConnection serverConnection = new ServerConnection(port, clientsManager, threadPool);
		ServerController serverController = new ServerController(serverConnection);
	}

}
