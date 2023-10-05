package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.threads.DisconnectedThread;
import eu.playsc.stonesocketapi.server.threads.ReceivedThread;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientTcpReadThread implements Runnable {
	private final Client client;
	private final Connection serverConnection;
	private ObjectInputStream in;

	public ClientTcpReadThread(Client client, Connection serverConnection) {
		this.client = client;
		this.serverConnection = serverConnection;

		try {
			in = new ObjectInputStream(serverConnection.getSocket().getInputStream());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public void run() {
		while (!serverConnection.getSocket().isClosed()) {
			try {
				Object object = in.readObject();

				if (!(object instanceof Packet)) {
					Logger.error("Received object is not a packet!");
					continue;
				}

				client.executeThread(new ReceivedThread(client.getListener(), serverConnection, (Packet) object));
			} catch (Exception ignored) {
				serverConnection.close();
				client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
			}
		}
	}
}
