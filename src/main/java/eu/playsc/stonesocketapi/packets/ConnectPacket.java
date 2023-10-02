package eu.playsc.stonesocketapi.packets;

public class ConnectPacket implements Packet {
	private final String identifier;

	public ConnectPacket(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
