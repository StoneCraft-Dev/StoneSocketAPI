package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
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
			Logger.error(e);
		}
	}

	@Override
	public void run() {
		byte[] data;
		while (con.getSocket() != null && !con.getSocket().isClosed() && in != null) {
			try {
				try {
					data = in.readAllBytes();
				} catch (Exception e) { //Client disconnected and data wasn't finished sending
					ConnectionManager.getInstance().close(con);
					in.close();
					in = null;
					return;
				}

				ByteArrayInputStream objIn = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(objIn);
				Object object = is.readObject();

				server.executeThread(new ReceivedThread(server.getListener(), con, object));

				is.close();
				objIn.close();
			} catch (IOException | ClassNotFoundException e) { //disconnected
				Logger.error(e);
				ConnectionManager.getInstance().close(con);
				try {
					in.close();
				} catch (IOException e1) {
					Logger.error(e1);
				}
				in = null;
			}
		}
	}

}
