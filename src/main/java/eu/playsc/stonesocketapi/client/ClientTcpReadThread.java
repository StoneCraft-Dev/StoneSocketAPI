package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.threads.DisconnectedThread;
import eu.playsc.stonesocketapi.server.threads.ReceivedThread;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
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
		try {
			ObjectInputStream in = new ObjectInputStream(serverConnection.getSocket().getInputStream());
			while (!serverConnection.getSocket().isClosed()) {
				byte[] data;
				try {
					data = IOUtils.toByteArray(in);
				} catch (Exception e) { //Connection lost to server and didn't finish sending data
					serverConnection.close();
					client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
					return; //kill thread
				}

				ByteArrayInputStream objIn = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(objIn);
				Object object = is.readObject();

				if (!(object instanceof Packet)) {
					Logger.error("Received object is not a packet!");
					continue;
				}

				client.executeThread(new ReceivedThread(client.getListener(), serverConnection, (Packet) object));
			}
		} catch (Exception e) {
			Logger.error(e);
			serverConnection.close();
			client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
		}
	}
}
