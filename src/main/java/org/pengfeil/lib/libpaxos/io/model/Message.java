package org.pengfeil.lib.libpaxos.io.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Base class for representing message between agent.
 *
 */
public abstract class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3755197480213667982L;
	protected Endpoint[] eps;
	protected MessageType messageType;
	
	public Message(MessageType messageType) {
		this.messageType = messageType;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Endpoint[] getEps() {
		return eps;
	}

	public void setEps(Endpoint[] eps) {
		this.eps = eps;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(eps);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (!Arrays.equals(eps, other.eps))
			return false;
		return true;
	}
	
}
