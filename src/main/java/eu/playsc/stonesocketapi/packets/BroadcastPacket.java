package eu.playsc.stonesocketapi.packets;

public class BroadcastPacket implements Packet {
	private final String identifier;
	private String message;

	public BroadcastPacket(String identifier) {
		this.identifier = identifier;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}
}
