package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpAcceptThread implements Runnable {
	private final ServerSocket tcpSocket;
	private final Server server;

	public TcpAcceptThread(Server server, ServerSocket tcpSocket) {
		this.tcpSocket = tcpSocket;
		this.server = server;
	}

	@Override
	public void run() {
		while (!tcpSocket.isClosed()) {
			try {
				server.executeThread(new AcceptedSocketThread(server, tcpSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
