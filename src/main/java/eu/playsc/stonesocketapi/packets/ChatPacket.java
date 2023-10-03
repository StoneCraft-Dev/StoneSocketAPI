package eu.playsc.stonesocketapi.packets;

public class ChatPacket implements Packet {
	private final String identifier;
	private String message;
	private String fromPlayer;
	private Type type;

	public ChatPacket(String originServer, Type type) {
		identifier = originServer;
		message = null;
		fromPlayer = null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromPlayer() {
		return fromPlayer;
	}

	public void setFromPlayer(String fromPlayer) {
		this.fromPlayer = fromPlayer;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		GLOBAL,
		STAFF
	}
}
