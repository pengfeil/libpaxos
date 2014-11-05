package org.pengfeil.lib.libpaxos.io;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;

/**
 * The communication interface for each agent. Agent will use such component for
 * reading message from other agents or sending message to others.
 */
public abstract class Communicator {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected Endpoint selfEndpoint;

	public Communicator(Endpoint selfEndpoint) {
		super();
		this.selfEndpoint = selfEndpoint;
	}
	
	/**
	 * Read message from a specific end point, with a timeout setting.
	 * 
	 * @param timeout
	 *            the max time to wait. Set to 0 for wait until data arrive.
	 * @return
	 */
	public abstract Message receive(long timeout, TimeUnit unit, Endpoint endpoint) throws Exception;

	/**
	 * Send messages to a specific end point (means agent).
	 * 
	 * @param msg
	 *            message to send.
	 */
	public abstract void send(Message msg);
	
	/**
	 * Listen for input connection/channel
	 * @param handler
	 */
	public abstract void listen(ListenHandler handler);

	public Endpoint getSelfEndpoint() {
		return selfEndpoint;
	}
	
	public static interface ListenHandler{
		public void handle(Message msg, Endpoint from);
	}
}
