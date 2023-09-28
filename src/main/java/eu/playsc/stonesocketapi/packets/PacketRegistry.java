package eu.playsc.stonesocketapi.packets;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class PacketRegistry {
	private static final String PACKET_PACKAGE = "eu.playsc.packets";
	private static final List<Class<? extends Packet>> registeredPackets = new ArrayList<>();

	public static void init() {
		final Reflections reflections = new Reflections(PACKET_PACKAGE);
		final Set<Class<? extends Packet>> packetClasses = reflections.getSubTypesOf(Packet.class);
		registeredPackets.addAll(packetClasses);
	}

	public static int indexOf(final Class<? extends Packet> packetClass) {
		return PacketRegistry.registeredPackets.indexOf(packetClass);
	}

	public static Class<? extends Packet> get(final int index) {
		return PacketRegistry.registeredPackets.get(index);
	}
}
