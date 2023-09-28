package eu.playsc.stonesocketapi.api;

import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.threads.ServerSocketThread;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

public class Server extends Connection {
	@Getter
	private final int port;
	private ServerSocket serverSocket;
	private ServerSocketThread serverSocketThread;

	public Server(final int port) {
		this.port = port;
	}

	@Override
	public void connect() throws IOException {
		if (this.serverSocket == null) {
			this.serverSocket = new ServerSocket(this.port);
		}

		this.serverSocketThread = new ServerSocketThread(this.serverSocket);
		this.serverSocketThread.start();
	}

	@Override
	public void disconnect() throws IOException {
		this.disconnectAllClients();
		if (!this.serverSocket.isClosed()) {
			this.serverSocket.close();
		}
	}

	public void sendToClient(final Packet packet, final UUID uuid) {
		this.serverSocketThread.sendToClient(packet, uuid);
	}

	public void sendToAllClients(final Packet packet) {
		this.serverSocketThread.sendToAllClients(packet);
	}

	public void disconnectClient(final UUID uuid) {
		this.serverSocketThread.disconnectClient(uuid);
	}

	public void disconnectAllClients() {
		this.serverSocketThread.disconnectAllClients();
	}
}
