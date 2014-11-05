package org.pengfeil.lib.libpaxos.configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configurations. Need to be moved to configuration file later.
 * 
 */
public class Configuration {
	public static class Communication {
		public static final long COMMUNICATE_TIMEOUT = 500;
		public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	}
}
