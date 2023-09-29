package eu.playsc.stonesocketapi.common.exceptions;

public class ClientNotConnectedException extends Exception {
	public ClientNotConnectedException(String message) {
		super("Client is not connected through TCP.");
	}
}
