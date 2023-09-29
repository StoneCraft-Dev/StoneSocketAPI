package eu.playsc.stonesocketapi.common.exceptions;

public class ClientCantConnectException extends Exception {
	public ClientCantConnectException() {
		super("Client can't connect to the server at the given IP Address with the given ports.");
	}
}
