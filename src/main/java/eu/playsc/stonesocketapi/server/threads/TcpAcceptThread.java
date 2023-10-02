package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
				Socket socket = tcpSocket.accept();

				Logger.log("Accepted connection from " + socket.getInetAddress().getHostAddress());
				Logger.log("Awaiting authentication...");
				server.executeThread(new AcceptedSocketThread(server, tcpSocket.accept()));
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}
}
