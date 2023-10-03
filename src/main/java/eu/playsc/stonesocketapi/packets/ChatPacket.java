package eu.playsc.stonesocketapi.packets;

import java.util.UUID;

public class ChatPacket implements Packet {
	private final String identifier;
	private String message;
	private String fromPlayer;
	private Type type;
	private UUID senderUUID;

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

	public UUID getSenderUUID() {
		return senderUUID;
	}

	public void setSenderUUID(UUID senderUUID) {
		this.senderUUID = senderUUID;
	}

	public enum Type {
		GLOBAL,
		STAFF
	}
}
