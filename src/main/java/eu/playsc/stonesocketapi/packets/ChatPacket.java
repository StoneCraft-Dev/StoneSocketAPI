package eu.playsc.stonesocketapi.packets;

import java.util.UUID;

public class ChatPacket implements Packet {
	private final String identifier;
	private final Type type;
	private final String message;
	private final String fromPlayer;
	private final UUID senderUUID;

	public ChatPacket(String originServer, Type type, String message, String fromPlayer, UUID senderUUID) {
		identifier = originServer;
		this.type = type;
		this.message = message;
		this.fromPlayer = fromPlayer;
		this.senderUUID = senderUUID;
	}

	public String getMessage() {
		return message;
	}

	public String getFromPlayer() {
		return fromPlayer;
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

	public enum Type {
		GLOBAL,
		STAFF
	}
}
