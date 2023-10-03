package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import eu.playsc.stonesocketapi.server.threads.DisconnectedThread;
import eu.playsc.stonesocketapi.server.threads.ReceivedThread;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
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
				byte[] data;
				try {
					data = IOUtils.toByteArray(in);
				} catch (Exception e) {
					ConnectionManager.getInstance().close(serverConnection);
					in.close();
					in = null;
					return;
				}

				ByteArrayInputStream objIn = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(objIn);
				Object object = is.readObject();
				is.close();
				objIn.close();

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
