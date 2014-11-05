package org.pengfeil.lib.libpaxos.io.model;
/**
 * POJO class for Preparation message 
 *
 */
public class Preparation extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7703904213622911934L;
	
	/**
	 * Sequence number of proposals.
	 */
	private long sequenceNumber;
	
	public Preparation() {
		super(MessageType.PREPARATION);
	}

	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return "Preparation [sequenceNumber=" + sequenceNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (int) (sequenceNumber ^ (sequenceNumber >>> 32));
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
		Preparation other = (Preparation) obj;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}
	
	
}
