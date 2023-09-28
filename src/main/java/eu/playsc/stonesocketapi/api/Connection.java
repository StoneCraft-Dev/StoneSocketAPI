package eu.playsc.stonesocketapi.api;

import lombok.Getter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public abstract class Connection {
	private final AtomicReference<UUID> uuid = new AtomicReference<>(UUID.randomUUID());

	public abstract void connect() throws IOException;

	public abstract void disconnect() throws IOException;
}
