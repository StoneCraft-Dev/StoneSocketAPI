package eu.playsc.stonesocketapi.threads;


import eu.playsc.stonesocketapi.api.Client;
import eu.playsc.stonesocketapi.buffer.WritingByteBuffer;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.packets.PacketRegistry;
import eu.playsc.stonesocketapi.packets.UpdateUUIDPacket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OutputStreamThread {
	private final Client client;
	private final Socket socket;
	private final List<Packet> packets = new ArrayList<>();
	private final Timer timer = new Timer();
	private OutputStream finalOutputStream;

	public OutputStreamThread(final Client client) {
		this.client = client;
		this.socket = this.client.getSocket();
	}

	public void run() throws IOException {
		this.finalOutputStream = this.socket.getOutputStream();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					if (OutputStreamThread.this.socket.isClosed()) {
						OutputStreamThread.this.interrupt();
						return;
					}

					if (OutputStreamThread.this.packets.isEmpty())
						return;

					final Packet packet = OutputStreamThread.this.packets.get(0);
					if (packet == null)
						return;

					OutputStreamThread.this.packets.remove(0);
					final WritingByteBuffer writingByteBuffer = new WritingByteBuffer();
					if (packet.getClass().equals(UpdateUUIDPacket.class)) {
						writingByteBuffer.writeInt(-2);
						writingByteBuffer.writeUUID(packet.getConnectionUUID());
					} else {
						final int packetId = PacketRegistry.indexOf(packet.getClass());
						writingByteBuffer.writeInt(packetId);
						writingByteBuffer.writeUUID(OutputStreamThread.this.client.getUuid().get());
						packet.send(writingByteBuffer);
					}

					try {
						final byte[] bytes = writingByteBuffer.toBytes();
						assert OutputStreamThread.this.finalOutputStream != null;
						OutputStreamThread.this.finalOutputStream.write(bytes.length);
						OutputStreamThread.this.finalOutputStream.write(bytes);
						OutputStreamThread.this.finalOutputStream.flush();
					} catch (final SocketException exception) {
						exception.printStackTrace();
					}
				} catch (final IOException | NullPointerException exception) {
					exception.printStackTrace();
				}
			}
		}, 0, 1);
	}

	public void interrupt() {
		try {
			this.finalOutputStream.close();
			this.timer.cancel();
		} catch (final IOException exception) {
			exception.printStackTrace();
		}
	}

	public void send(final Packet packet) {
		this.packets.add(packet);
	}
}
