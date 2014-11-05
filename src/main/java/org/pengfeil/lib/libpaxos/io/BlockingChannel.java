package org.pengfeil.lib.libpaxos.io;

import org.pengfeil.lib.libpaxos.io.model.Endpoint;


/**
 * Interface for communication channel between agents
 *
 */
public abstract class BlockingChannel extends Channel {

	public BlockingChannel(Endpoint from, Endpoint to) {
		super(from, to);
	}
}
