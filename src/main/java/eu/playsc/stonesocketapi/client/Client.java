package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.IProtocol;
import eu.playsc.stonesocketapi.common.SocketListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements IProtocol {
	private final int tcpPort;
	private InetAddress address;
	private ExecutorService mainExecutor;
	private Socket tcpSocket;
	private SocketListener listener;
	private Connection serverConnection;

	public Client(String address, int tcpPort) {
		try {
			if (address.equalsIgnoreCase("localhost"))
				this.address = InetAddress.getLocalHost();
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			Logger.error(e);
		}

		this.tcpPort = tcpPort;
	}

	@Override
	public SocketListener getListener() {
		return listener;
	}

	public void setListener(SocketListener listener) {
		this.listener = listener;
	}

	public Connection getServerConnection() {
		return serverConnection;
	}

	public void connect() {
		try {
			tcpSocket = new Socket(address, tcpPort);
			serverConnection = new Connection(tcpSocket);
			serverConnection.setProtocol(this);
			if (listener != null) {
				listener.connected(serverConnection);
			}
		} catch (IOException e) {
			Logger.error(e);
		}

		mainExecutor = Executors.newCachedThreadPool();

		if (isConnected()) {
			mainExecutor.execute(new ClientTcpReadThread(this, serverConnection));
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
