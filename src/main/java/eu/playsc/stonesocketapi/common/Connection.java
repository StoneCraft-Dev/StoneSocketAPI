package eu.playsc.stonesocketapi.common;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.ConnectionManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	private static int counter = 0;
	private final int id;
	private transient final InetAddress address;
	private transient ObjectOutputStream outputStream = null;
	private transient Socket socket;
	private IProtocol protocol;

	public Connection(Socket socket) {
		this.socket = socket;
		address = socket.getInetAddress();

		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Logger.error(e);
		}

		id = ++counter;
	}

	public int getId() {
		return id;
	}

	public InetAddress getAddress() {
		return address;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectOutputStream getOutput() {
		return outputStream;
	}

	public void setProtocol(IProtocol protocol) {
		this.protocol = protocol;
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected() && socket.isBound() && !socket.isClosed();
	}

	public void sendPacket(Packet packet) {
		try {
			outputStream.writeObject(packet);
			outputStream.flush();
		} catch (IOException e) {
			Logger.error(e);
			if (protocol.getListener() != null)
				protocol.getListener().disconnected(this);
			ConnectionManager.getInstance().close(this);
		}
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			//socket already closed
		}
		socket = null;
	}
}
