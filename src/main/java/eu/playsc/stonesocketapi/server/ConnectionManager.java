package eu.playsc.stonesocketapi.server;

import eu.playsc.stonesocketapi.common.Connection;
import lombok.Getter;

import java.net.InetAddress;
import java.util.ArrayList;

public class ConnectionManager {
	@Getter
	private static final ConnectionManager instance = new ConnectionManager();
	@Getter
	private final ArrayList<Connection> connections = new ArrayList<>();

	private ConnectionManager() {

	}

	public Connection getConnection(InetAddress address) {
		for (Connection con : connections) {
			if (con.getAddress().equals(address)) {
				return con;
			}
		}
		return null;
	}

	public void addConnection(Connection con) {
		connections.add(con);
	}

	public void close(Connection con) {
		con.close();
		connections.remove(con);
	}

	public void closeAll() {
		for (Connection con : connections) {
			con.close();
		}
	}
}
