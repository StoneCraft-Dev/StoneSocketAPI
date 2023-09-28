package eu.playsc.stonesocketapi.api;

import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.threads.InputStreamThread;
import eu.playsc.stonesocketapi.threads.OutputStreamThread;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {
	@Getter
	private String hostname;
	@Getter
	private int port;
	@Getter
	private Socket socket;
	private InputStreamThread inputStreamThread;
	private OutputStreamThread outputStreamThread;

	public Client(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public Client(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void connect() throws IOException {
		if (this.socket == null) {
			this.socket = new Socket(this.hostname, this.port);
			this.socket.setKeepAlive(true);
		}
		
		this.inputStreamThread = new InputStreamThread(this);
		this.inputStreamThread.run();
		this.outputStreamThread = new OutputStreamThread(this);
		this.outputStreamThread.run();
	}

	@Override
	public void disconnect() throws IOException {
		this.inputStreamThread.interrupt();
		this.outputStreamThread.interrupt();

		if (!this.socket.isClosed()) {
			this.socket.close();
		}
	}

	public void send(final Packet packet) {
		this.outputStreamThread.send(packet);
	}
}
