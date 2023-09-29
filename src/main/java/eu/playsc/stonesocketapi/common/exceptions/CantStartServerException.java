package eu.playsc.stonesocketapi.common.exceptions;

public class CantStartServerException extends Exception {
	public CantStartServerException() {
		super("Can't start the server socket on the desired port. Is something already bound to it?");
	}
}
