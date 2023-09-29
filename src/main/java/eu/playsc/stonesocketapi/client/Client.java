package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.IProtocol;
import eu.playsc.stonesocketapi.common.SocketListener;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements IProtocol {
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
			e.printStackTrace();
		}
		this.tcpPort = tcpPort;
	}

	public Client() {
		address = null;
		tcpPort = -1;
		mainExecutor = Executors.newCachedThreadPool();
	}

	public void connect() {
		try {
			tcpSocket = new Socket(address, tcpPort);
			serverConnection = new Connection(tcpSocket);
			serverConnection.setProtocol(this);
			if (listener != null) {
				listener.connected(serverConnection);
			}
			ConnectionManager.getInstance().addConnection(serverConnection);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mainExecutor = Executors.newCachedThreadPool();

		if (tcpSocket != null && tcpSocket.isConnected()) {
			mainExecutor.execute(new ClientTcpReadThread(this, serverConnection));
			serverConnection.sendTcp("ConnectedToServer");
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
			e.printStackTrace();
		}
	}
}
