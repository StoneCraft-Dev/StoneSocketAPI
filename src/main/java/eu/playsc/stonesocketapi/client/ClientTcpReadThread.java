package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.threads.DisconnectedThread;
import eu.playsc.stonesocketapi.server.threads.ReceivedThread;

import java.io.ObjectInputStream;

public class ClientTcpReadThread implements Runnable {
	private final Client client;
	private final Connection serverConnection;

	public ClientTcpReadThread(Client client, Connection serverConnection) {
		this.client = client;
		this.serverConnection = serverConnection;
	}

	@Override
	public void run() {
		while (!serverConnection.getSocket().isClosed()) {
			try (ObjectInputStream is = new ObjectInputStream(serverConnection.getSocket().getInputStream())) {
				Object object = is.readObject();

				if (!(object instanceof Packet)) {
					Logger.error("Received object is not a packet!");
					continue;
				}

				client.executeThread(new ReceivedThread(client.getListener(), serverConnection, (Packet) object));
			} catch (Exception e) {
				Logger.error(e);
				serverConnection.close();
				client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
			}
		}
	}
}
