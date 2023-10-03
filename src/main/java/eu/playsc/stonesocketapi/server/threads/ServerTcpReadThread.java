package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.packets.AuthPacket;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import eu.playsc.stonesocketapi.server.Server;
import org.apache.commons.io.IOUtils;

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
		while (con.getSocket() != null && !con.getSocket().isClosed() && in != null) {
			try {
				byte[] data;
				try {
					data = IOUtils.toByteArray(in);
				} catch (Exception e) {
					ConnectionManager.getInstance().close(con);
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

				if (object instanceof AuthPacket) {
					AuthPacket authPacket = (AuthPacket) object;
					if (authPacket.getAuthKey().equals(server.getKey())) {
						Logger.log("Accepted authentication from " + con.getAddress().getHostAddress());
						//con.sendPacket(new AuthSuccessPacket());
						ConnectionManager.getInstance().authenticateConnection(con);
						server.executeThread(new NewConnectionThread(server.getListener(), con));
					} else {
						Logger.log("Denied authentication from " + con.getAddress().getHostAddress() + " with key " + authPacket.getAuthKey());
						//con.sendPacket(new AuthDenyPacket());
						ConnectionManager.getInstance().close(con);
						try {
							in.close();
						} catch (IOException e1) {
							Logger.error(e1);
						}
						in = null;
						continue;
					}

					continue;
				}

				if (!ConnectionManager.getInstance().isConnectionAuthenticated(con))
					continue;

				server.executeThread(new ReceivedThread(server.getListener(), con, (Packet) object));
			} catch (Exception e) {
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
