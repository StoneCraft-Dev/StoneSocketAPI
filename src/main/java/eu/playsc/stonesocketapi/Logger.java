package eu.playsc.stonesocketapi;

import java.util.logging.Level;

public class Logger {
	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getAnonymousLogger();
	public static void error(Exception e) {
		LOGGER.log(Level.SEVERE, e.getMessage(), e);
	}

	public static void error(String s) {
		LOGGER.log(Level.SEVERE, s);
	}

	public static void log(String s) {
		LOGGER.log(Level.INFO, s);
	}

	public static void warn(String s) {
		LOGGER.log(Level.WARNING, s);
	}
}
