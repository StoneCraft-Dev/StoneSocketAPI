package eu.playsc.stonesocketapi.threads;


import eu.playsc.stonesocketapi.api.Client;
import eu.playsc.stonesocketapi.packets.Packet;
import eu.playsc.stonesocketapi.packets.UpdateUUIDPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerSocketThread extends Thread {
	private final ServerSocket serverSocket;
	private final List<Client> clients = new ArrayList<>();

	public ServerSocketThread(final ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		super.run();
		try {
			while (true) {
				if (this.serverSocket.isClosed()) {
					this.interrupt();
					break;
				} else {
					final Socket socket = this.serverSocket.accept();
					final Client client = new Client(socket);
					client.connect();
					this.clients.add(client);
					final UpdateUUIDPacket updateUUIDPacket = new UpdateUUIDPacket(client.getUuid().get());
					System.out.println("Client: " + client.getUuid().get() + " has connected!");
					client.send(updateUUIDPacket);
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToClient(final Packet packet, final UUID uuid) {
		this.clients.stream().filter(client -> client.getUuid().get().equals(uuid)).forEach(client -> client.send(packet));
	}

	public void sendToAllClients(final Packet packet) {
		this.clients.forEach(client -> client.send(packet));
	}

	public void disconnectClient(final UUID uuid) {
		this.clients.stream().filter(client -> client.getUuid().get().equals(uuid)).forEach(client -> {
			try {
				System.out.println("Client: " + client.getUuid().get() + " will be disconnected!");
				client.disconnect();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void disconnectAllClients() {
		System.out.println("All Clients will be disconnected!");
		this.clients.forEach(client -> {
			try {
				if (client != null) {
					client.disconnect();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
		this.clients.clear();
	}
}
