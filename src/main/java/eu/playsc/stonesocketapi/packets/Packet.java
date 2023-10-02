package eu.playsc.stonesocketapi.packets;

import java.io.Serializable;

public interface Packet extends Serializable {
	String getOriginServer();
}
