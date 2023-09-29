package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String key = in.readLine();
				if (!key.equals(server.getKey())) {
					Logger.warn("Invalid key (" + key + ") from " + socket.getInetAddress().getHostAddress());
					socket.close();
					continue;
				}

				Logger.log("Accepted connection from " + socket.getInetAddress().getHostAddress());
				server.executeThread(new AcceptedSocketThread(server, tcpSocket.accept()));
			} catch (IOException e) {
				Logger.error(e);
			}
		}
	}
}
