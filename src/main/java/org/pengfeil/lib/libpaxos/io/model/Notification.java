package org.pengfeil.lib.libpaxos.io.model;
/**
 * POJO for a notification message to learner. 
 *
 */
public class Notification extends Message {
	private static final long serialVersionUID = 5977687307308473192L;
	
	public Notification() {
		super(MessageType.NOTIFICATION);
	}
	
	private Proposal acceptedProposal;

	public Proposal getAcceptedProposal() {
		return acceptedProposal;
	}

	public void setAcceptedProposal(Proposal acceptedProposal) {
		this.acceptedProposal = acceptedProposal;
	}

	@Override
	public String toString() {
		return "Notification [acceptedProposal=" + acceptedProposal + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((acceptedProposal == null) ? 0 : acceptedProposal.hashCode());
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
		Notification other = (Notification) obj;
		if (acceptedProposal == null) {
			if (other.acceptedProposal != null)
				return false;
		} else if (!acceptedProposal.equals(other.acceptedProposal))
			return false;
		return true;
	}
	
}
