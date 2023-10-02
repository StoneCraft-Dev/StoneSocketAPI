package eu.playsc.stonesocketapi.common;

import eu.playsc.stonesocketapi.packets.Packet;

public interface SocketListener {
	void received(Connection con, Packet packet);

	void connected(Connection con);

	void disconnected(Connection con);

}
