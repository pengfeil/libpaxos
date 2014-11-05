package org.pengfeil.lib.libpaxos.io.model;

import java.util.Arrays;

/**
 * POJO class for Proposal message 
 *
 */
public class Proposal extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2850204468731224517L;

	/**
	 * Content of proposal
	 */
	protected byte[] content;
	
	private long sequenceNumber;

	public Proposal(byte[] content) {
		super(MessageType.PROPOSAL);
		this.content = content;
	}
	
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Proposal [content=" + new String(content) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(content);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposal other = (Proposal) obj;
		if (!Arrays.equals(content, other.content))
			return false;
		return true;
	}
	
	
}
