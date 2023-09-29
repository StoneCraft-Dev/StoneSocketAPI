package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.SocketListener;

public class ReceivedThread implements Runnable {
	private final SocketListener listener;
	private final Connection con;
	private final Object object;

	public ReceivedThread(SocketListener listener, Connection con, Object object) {
		this.listener = listener;
		this.con = con;
		this.object = object;
	}

	@Override
	public void run() {
		if (listener != null) {
			listener.received(con, object);
		}
	}
}
