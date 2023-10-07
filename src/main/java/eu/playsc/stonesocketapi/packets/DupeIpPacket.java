package eu.playsc.stonesocketapi.packets;

import java.util.List;
import java.util.UUID;

public class DupeIpPacket implements Packet {
	private final String originServer;
	private final UUID uuid;
	private final List<UUID> alternateAccounts;


	public DupeIpPacket(String originServer, UUID uuid, List<UUID> alternateAccounts) {
		this.originServer = originServer;
		this.uuid = uuid;
		this.alternateAccounts = alternateAccounts;
	}

	public UUID getUuid() {
		return uuid;
	}

	public List<UUID> getAlternateAccounts() {
		return alternateAccounts;
	}

	@Override
	public String getOriginServer() {
		return originServer;
	}
}
