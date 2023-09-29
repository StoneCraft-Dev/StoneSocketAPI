package eu.playsc.stonesocketapi.packets;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ConnectedToServerPacket implements Serializable {
	private final String identifier;
	private final String key;

	public ConnectedToServerPacket(String identifier, String key) {
		this.identifier = identifier;
		this.key = key;
	}
}
