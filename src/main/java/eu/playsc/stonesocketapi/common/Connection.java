package eu.playsc.stonesocketapi.common;

import eu.playsc.stonesocketapi.server.ConnectionManager;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	private static int counter = 0;
	@Getter
	private final int id;
	@Getter
	private transient final InetAddress address;
	@Getter
	private transient Socket socket;
	private transient ObjectOutputStream tcpOut;
	private IProtocol protocol;

	public Connection(Socket socket) {
		this.socket = socket;
		address = socket.getInetAddress();

		try {
			socket.setSoLinger(true, 0);
			tcpOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		id = ++counter;
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected() && socket.isBound() && !socket.isClosed();
	}

	public void sendTcp(Object object) {
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteOutStream);
			objOut.writeObject(object);
			byte[] data = PacketUtils.getByteArray(byteOutStream);
			objOut.close();
			byteOutStream.close();
			synchronized(tcpOut) {
				tcpOut.write(data);
				tcpOut.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (protocol.getListener() != null && protocol.getListener() != null)
				protocol.getListener().disconnected(this);
			ConnectionManager.getInstance().close(this);
		}
	}

	public ObjectOutputStream getTcpOutputStream() {
		return tcpOut;
	}

	public void setProtocol(IProtocol protocol) {
		this.protocol = protocol;
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
