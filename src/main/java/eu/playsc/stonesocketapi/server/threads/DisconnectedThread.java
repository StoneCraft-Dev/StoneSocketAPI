package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.SocketListener;

public class DisconnectedThread implements Runnable {
	private final SocketListener listener;
	private final Connection con;

	public DisconnectedThread(SocketListener listener, Connection con) {
		this.listener = listener;
		this.con = con;
	}

	@Override
	public void run() {
		if (listener != null)
			listener.disconnected(con);
	}
}
