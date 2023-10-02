package eu.playsc.stonesocketapi.packets;

import lombok.Getter;
import lombok.Setter;

public class BroadcastPacket implements Packet {
	private final String identifier;
	@Getter
	@Setter
	private String message;

	public BroadcastPacket(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
