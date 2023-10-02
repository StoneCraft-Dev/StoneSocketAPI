package eu.playsc.stonesocketapi.packets;

public class AuthPacket implements Packet {
	private final String identifier;
	private final String authKey;

	public AuthPacket(String identifier, String authKey) {
		this.identifier = identifier;
		this.authKey = authKey;
	}

	@Override
	public String getOriginServer() {
		return identifier;
	}

	public String getAuthKey() {
		return authKey;
	}
}
