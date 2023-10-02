package eu.playsc.stonesocketapi.packets;

public class DisconnectPacket implements Packet {
	private final String identifier;

	public DisconnectPacket(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
