package org.pengfeil.lib.libpaxos.io;

import org.pengfeil.lib.libpaxos.io.model.Message;


/**
 * Interface for communication channel between agents
 *
 */
public interface IBlockingChannel {
	public Message read();
	public boolean send(Message msg);
}
