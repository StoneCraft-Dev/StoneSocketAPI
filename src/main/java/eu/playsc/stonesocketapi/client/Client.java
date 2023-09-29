package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.IProtocol;
import eu.playsc.stonesocketapi.common.SocketListener;
import eu.playsc.stonesocketapi.packets.ConnectedToServerPacket;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements IProtocol {
	private String identifier;
	private String key;
	private final int tcpPort;
	private InetAddress address;
	private ExecutorService mainExecutor;
	private Socket tcpSocket;
	@Getter
	@Setter
	private SocketListener listener;
	@Getter
	private Connection serverConnection;
	public Client(String address, int tcpPort) {
		try {
			if (address.equalsIgnoreCase("localhost"))
				address = InetAddress.getLocalHost().getHostName();
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			Logger.error(e);
		}
		this.tcpPort = tcpPort;
	}

	public void connect(String identifier, String key) {
		this.identifier = identifier;
		this.key = key;

		try {
			tcpSocket = new Socket(address, tcpPort);
			PrintWriter writer = new PrintWriter(tcpSocket.getOutputStream(), true);
			writer.println(this.key);

			serverConnection = new Connection(tcpSocket);
			serverConnection.setProtocol(this);
			if (listener != null) {
				listener.connected(serverConnection);
			}
			ConnectionManager.getInstance().addConnection(serverConnection);
		} catch (IOException e) {
			Logger.error(e);
		}

		mainExecutor = Executors.newCachedThreadPool();

		if (isConnected()) {
			mainExecutor.execute(new ClientTcpReadThread(this, serverConnection));
			serverConnection.sendTcp(new ConnectedToServerPacket(identifier, key));
			Logger.log("Connected to server!");
		} else {
			Logger.error("Can't connect to server! Maybe wrong key?");
		}
	}

	@Override
	public boolean isConnected() {
		return tcpSocket != null && tcpSocket.isConnected() && tcpSocket.isBound() && !tcpSocket.isClosed();
	}

	@Override
	public void executeThread(Runnable run) {
		mainExecutor.execute(run);
	}

	@Override
	public void close() {
		try {
			tcpSocket.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
