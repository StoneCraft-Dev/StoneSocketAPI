package eu.playsc.stonesocketapi.threads;


import eu.playsc.stonesocketapi.api.Client;
import eu.playsc.stonesocketapi.buffer.ReadingByteBuffer;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.packets.PacketRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class InputStreamThread {
	private final Client client;
	private final Socket socket;
	private final Timer timer = new Timer();
	private InputStream finalInputStream;
	final AtomicReference<byte[]> bytes = new AtomicReference<>(null);

	public InputStreamThread(final Client client) {
		this.client = client;
		this.socket = this.client.getSocket();
	}

	public void run() throws IOException {
		this.finalInputStream = this.socket.getInputStream();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					if (InputStreamThread.this.socket.isClosed()) {
						InputStreamThread.this.interrupt();
						return;
					}

					if (InputStreamThread.this.finalInputStream == null)
						return;

					if (InputStreamThread.this.finalInputStream.available() <= 0)
						return;

					final int b = InputStreamThread.this.finalInputStream.read();
					if (b != -1) {
						InputStreamThread.this.bytes.set(new byte[b]);
						InputStreamThread.this.finalInputStream.read(InputStreamThread.this.bytes.get(), 0, b);
						final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(InputStreamThread.this.bytes.get());
						final int packetId = readingByteBuffer.readInt();

						if (packetId == -2) {
							final UUID connectionUUID = readingByteBuffer.readUUID();
							InputStreamThread.this.client.getUuid().set(connectionUUID);
						} else {
							final Class<? extends Packet> packet = PacketRegistry.get(packetId);
							final UUID connectionUUID = readingByteBuffer.readUUID();
							packet.getConstructor(UUID.class).newInstance(connectionUUID).receive(readingByteBuffer);
						}
					} else {
						InputStreamThread.this.socket.close();
					}
				} catch (final InstantiationException | IllegalAccessException | NoSuchMethodException |
							   InvocationTargetException exception) {
					exception.printStackTrace();
				} catch (final IOException ignored) {
					InputStreamThread.this.interrupt();
				}
			}
		}, 0, 1);
	}

	public void interrupt() {
		try {
			this.finalInputStream.close();
			this.timer.cancel();
		} catch (final IOException exception) {
			exception.printStackTrace();
		}
	}
}
