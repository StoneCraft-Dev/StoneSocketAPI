package eu.playsc.stonesocketapi.packets;

public class ChatPacket implements Packet {
	private final String identifier;
	private String message;
	private String fromPlayer;
	private String toPlayer;

	public ChatPacket(String identifier) {
		this.identifier = identifier;
		message = null;
		fromPlayer = null;
		toPlayer = null;
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

	public String getToPlayer() {
		return toPlayer;
	}

	public void setToPlayer(String toPlayer) {
		this.toPlayer = toPlayer;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
