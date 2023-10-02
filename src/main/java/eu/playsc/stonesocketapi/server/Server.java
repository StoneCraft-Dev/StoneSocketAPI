package eu.playsc.stonesocketapi.server;

import eu.playsc.stonesocketapi.Logger;
import eu.playsc.stonesocketapi.common.IProtocol;
import eu.playsc.stonesocketapi.common.SocketListener;
import eu.playsc.stonesocketapi.common.exceptions.CantStartServerException;
import eu.playsc.stonesocketapi.server.threads.TcpAcceptThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IProtocol {
	private final ExecutorService mainExecutor;
	private final ServerSocket tcpSocket;
	private final String key;
	private SocketListener listener;

	public Server(int tcpPort, String key) throws CantStartServerException {
		this.key = key;

		try {
			tcpSocket = new ServerSocket(tcpPort, 1, InetAddress.getByName("0.0.0.0"));
		} catch (IOException e) {
			throw new CantStartServerException();
		}

		mainExecutor = Executors.newCachedThreadPool();
		mainExecutor.execute(new TcpAcceptThread(this, tcpSocket));
	}

	@Override
	public SocketListener getListener() {
		return listener;
	}

	public void setListener(SocketListener listener) {
		this.listener = listener;
	}

	public String getKey() {
		return key;
	}

	@Override
	public boolean isConnected() {
		return tcpSocket != null;
	}

	@Override
	public void executeThread(Runnable run) {
		mainExecutor.execute(run);
	}

	@Override
	public void close() {
		try {
			if (!tcpSocket.isClosed())
				tcpSocket.close();
		} catch (IOException e) {
			Logger.error(e);
		}
		ConnectionManager.getInstance().closeAll();
	}
}
