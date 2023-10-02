package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.server.ConnectionManager;
import eu.playsc.stonesocketapi.server.Server;
import eu.playsc.stonesocketapi.common.Connection;

import java.net.Socket;

public class AcceptedSocketThread implements Runnable {
	private final Socket socket;
	private final Server server;

	public AcceptedSocketThread(Server server, Socket socket) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		Connection con = new Connection(socket);
		con.setProtocol(server);
		ConnectionManager.getInstance().addConnection(con);
		server.executeThread(new ServerTcpReadThread(server, con));
	}
}
