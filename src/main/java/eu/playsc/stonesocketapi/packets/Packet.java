package eu.playsc.stonesocketapi.packets;

import eu.playsc.stonesocketapi.buffer.ReadingByteBuffer;
import eu.playsc.stonesocketapi.buffer.WritingByteBuffer;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Packet {

	private final UUID connectionUUID;

	public Packet(final UUID connectionUUID) {
		this.connectionUUID = connectionUUID;
	}

	public abstract void send(WritingByteBuffer writingByteBuffer);

	public abstract void receive(ReadingByteBuffer readingByteBuffer);

}
