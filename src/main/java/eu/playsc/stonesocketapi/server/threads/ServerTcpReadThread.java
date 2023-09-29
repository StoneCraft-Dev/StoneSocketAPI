package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.PacketUtils;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import eu.playsc.stonesocketapi.server.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ServerTcpReadThread implements Runnable {
	private final Connection con;
	private final Server server;
	private ObjectInputStream in;

	public ServerTcpReadThread(Server server, Connection con) {
		this.con = con;
		this.server = server;
		try {
			in = new ObjectInputStream(con.getSocket().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		byte[] data;
		while(con.getSocket() != null && !con.getSocket().isClosed() && in != null) {
			try {
				data = new byte[2048];
				try {
					in.readFully(data);
				} catch (Exception e) { //Client disconnected and data wasn't finished sending
					ConnectionManager.getInstance().close(con);
					in.close();
					in = null;
					return;
				}

				byte[] objectArray = PacketUtils.getObjectFromPacket(data);
				if (objectArray != null) {
					ByteArrayInputStream objIn = new ByteArrayInputStream(objectArray);
					ObjectInputStream is = new ObjectInputStream(objIn);
					Object object = is.readObject();

					if (!(object instanceof String)) {
						server.executeThread(new ReceivedThread(server.getListener(), con, object));
					} else if (!((String) object).equalsIgnoreCase("ConnectedToServer") && !((String)object).equalsIgnoreCase("TestAlivePing")) {
						server.executeThread(new ReceivedThread(server.getListener(), con, object));
					}
					is.close();
					objIn.close();
				}
			} catch (IOException | ClassNotFoundException e) { //disconnected
				e.printStackTrace();
				ConnectionManager.getInstance().close(con);
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				in = null;
			}
		}
	}

}
