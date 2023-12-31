package eu.playsc.stonesocketapi.server.threads;

import eu.playsc.stonesocketapi.common.Connection;
import eu.playsc.stonesocketapi.common.SocketListener;
import eu.playsc.stonesocketapi.packets.Packet;

public class ReceivedThread implements Runnable {
	private final SocketListener listener;
	private final Connection con;
	private final Packet packet;

	public ReceivedThread(SocketListener listener, Connection con, Packet packet) {
		this.listener = listener;
		this.con = con;
		this.packet = packet;
	}

	@Override
	public void run() {
		if (listener != null) {
			listener.received(con, packet);
		}
	}
}
