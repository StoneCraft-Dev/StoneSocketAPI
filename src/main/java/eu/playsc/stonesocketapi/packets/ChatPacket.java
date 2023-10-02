package eu.playsc.stonesocketapi.packets;

import lombok.Getter;
import lombok.Setter;

public class ChatPacket implements Packet {
	private final String identifier;
	@Getter
	@Setter
	private String message;
	@Getter
	@Setter
	private String fromPlayer;
	@Getter
	@Setter
	private String toPlayer;

	public ChatPacket(String identifier) {
		this.identifier = identifier;
		this.message = null;
		this.fromPlayer = null;
		this.toPlayer = null;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
