package eu.playsc.stonesocketapi.packets;

import java.util.UUID;

public class PlayerLoggedInPacket implements Packet {
	private final UUID uuid;
	private final String originServer;

	public PlayerLoggedInPacket(UUID uuid, String originServer) {
		this.uuid = uuid;
		this.originServer = originServer;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public String getOriginServer() {
		return originServer;
	}
}
