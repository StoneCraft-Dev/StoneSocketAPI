package eu.playsc.stonesocketapi.client;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.PacketUtils;
import eu.playsc.stonesocketapi.server.threads.DisconnectedThread;
import eu.playsc.stonesocketapi.server.threads.ReceivedThread;

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
				byte[] data = new byte[2048];
				try {
					in.readFully(data);
				} catch (Exception e) { //Connection lost to server and didn't finish sending data
					serverConnection.close();
					client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
					return; //kill thread
				}

				byte[] objectArray = PacketUtils.getObjectFromPacket(data);

				ByteArrayInputStream objIn = new ByteArrayInputStream(objectArray);
				ObjectInputStream is = new ObjectInputStream(objIn);
				Object object = is.readObject();

				if (object != null) {
					client.executeThread(new ReceivedThread(client.getListener(), serverConnection, object));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			serverConnection.close();
			client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
		}
	}

}
