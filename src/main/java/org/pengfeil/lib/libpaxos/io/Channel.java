package org.pengfeil.lib.libpaxos.io;

import org.apache.log4j.Logger;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;

public abstract class Channel {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected Endpoint from;
	protected Endpoint to;
	
	public Channel(Endpoint from, Endpoint to) {
		this.from = from;
		this.to = to;
	}
	public Endpoint getFrom() {
		return from;
	}
	public Endpoint getTo() {
		return to;
	}
	/**
	 * Interface for read message
	 * @return
	 */
	public abstract Message read();
	/**
	 * Interface for write message
	 * @param msg
	 */
	public abstract boolean send(Message msg);
	@Override
	public String toString() {
		return "Channel [from=" + from + ", to=" + to + "]";
	}
	public long count(){
		throw new UnsupportedOperationException("Not support count operation.");
	}
	
}
