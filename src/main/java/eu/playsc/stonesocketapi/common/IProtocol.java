package eu.playsc.stonesocketapi.common;

import java.net.DatagramSocket;

public interface IProtocol {
	void executeThread(Runnable run);
	SocketListener getListener();
	boolean isConnected();
	void close();

}
