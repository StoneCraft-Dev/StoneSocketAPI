package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.SocketListener;
import eu.playsc.stonesocketapi.server.ConnectionManager;
import eu.playsc.stonesocketapi.server.Server;

public class PingThread implements Runnable {
	private final Connection con;
	private final Server server;

	public PingThread(Server server, Connection con) {
		this.con = con;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			String s = "TestAlivePing";
			while (!con.getSocket().isClosed()) {
				con.sendTcp(s);
				Thread.sleep(5000);
			}
		} catch (NullPointerException | InterruptedException e) {
			server.executeThread(new DisconnectedThread((SocketListener)server.getListener(), con));
			ConnectionManager.getInstance().close(con);
		}
	}

}
