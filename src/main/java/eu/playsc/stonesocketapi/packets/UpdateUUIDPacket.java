package eu.playsc.stonesocketapi.packets;


import eu.playsc.stonesocketapi.buffer.ReadingByteBuffer;
import eu.playsc.stonesocketapi.buffer.WritingByteBuffer;

import java.util.UUID;

public class UpdateUUIDPacket extends Packet {
	public UpdateUUIDPacket(final UUID connectionUUID) {
		super(connectionUUID);
	}

	@Override
	public void send(final WritingByteBuffer writingByteBuffer) {

	}

	@Override
	public void receive(final ReadingByteBuffer readingByteBuffer) {

	}
}
