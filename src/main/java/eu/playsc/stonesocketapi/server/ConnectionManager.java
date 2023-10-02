package eu.playsc.stonesocketapi.server;

import eu.playsc.stonesocketapi.common.Connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
	private static final ConnectionManager instance = new ConnectionManager();
	private final List<Connection> connections = new ArrayList<>();
	private final List<Connection> authenticatedConnections = new ArrayList<>();

	private ConnectionManager() {
	}

	public static ConnectionManager getInstance() {
		return instance;
	}

	public List<Connection> getConnections() {
		return new ArrayList<>(authenticatedConnections);
	}

	public void addConnection(Connection con) {
		connections.add(con);
	}

	public void authenticateConnection(Connection con) {
		if (!authenticatedConnections.contains(con))
			authenticatedConnections.add(con);
		connections.remove(con);
	}

	public boolean isConnectionAuthenticated(Connection con) {
		return authenticatedConnections.contains(con);
	}

	public void close(Connection con) {
		con.close();
		connections.remove(con);
		authenticatedConnections.remove(con);
	}

	public void closeAll() {
		for (Connection con : connections) {
			con.close();
		}

		for (Connection con : authenticatedConnections) {
			con.close();
		}
	}
}
