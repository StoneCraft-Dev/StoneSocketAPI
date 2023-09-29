package eu.playsc.stonesocketapi.common;

public interface SocketListener {
	void received(Connection con, Object object);
	void connected(Connection con);
	void disconnected(Connection con);

}
