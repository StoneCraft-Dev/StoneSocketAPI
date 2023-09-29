package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.SocketListener;

public class NewConnectionThread implements Runnable {
	private final SocketListener listener;
	private final Connection con;

	public NewConnectionThread(SocketListener listener, Connection con) {
		this.listener = listener;
		this.con = con;
	}

	@Override
	public void run() {
		if (listener != null) {
			listener.connected(con);
		}
	}
}
