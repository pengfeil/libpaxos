package org.pengfeil.lib.libpaxos.agent;

import org.pengfeil.lib.libpaxos.configuration.Configuration;
import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Preparation;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

/**
 * A template class for implementing proposer.
 * 
 */
public abstract class AbstractProposer extends AbstractAgent implements
		IProposer {
	/**
	 * Construct a proposer.
	 * 
	 * @param commonicator
	 */
	public AbstractProposer(Communicator commonicator) {
		super(commonicator);
	}

	/**
	 * Steps: 1. Pick a sequence number for proposal. 2. Send a prepare request
	 * to minority of acceptors. 3a. If current sequence number is smaller than
	 * the max sequence number from response of acceptors, drop the proposal.
	 * 3b. Else send the proposal.
	 */
	public void propose() {
		/**
		 * Generate propose messages.
		 */
		Proposal proposal = new Proposal(generateProposalContent());
		Endpoint[] acceptors = getMajority(this.acceptors);
		proposal.setEps(acceptors);
		proposal.setSequenceNumber(generateProposalSeqenceNumber());
		/**
		 * Send preparation request
		 */
		long maxSeqenceNumFromAcceptors = prepare(proposal);
		if(maxSeqenceNumFromAcceptors < 0){
			//Means receive response failed.
			return;
		}
		/**
		 * Only send when current sequence number is bigger than accepted
		 * number.
		 */
		if (maxSeqenceNumFromAcceptors <= proposal.getSequenceNumber()) {
			try {
				logger.info("send proposal with sequence number:"
						+ proposal.getSequenceNumber());
				commonicator.send(proposal);
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		} else {
			logger.warn("A higher sequence number["
					+ maxSeqenceNumFromAcceptors
					+ "] has been accepted, drop current proposal["
					+ proposal.getSequenceNumber() + "].");
		}
	}

	protected long prepare(Proposal proposal) {
		logger.info("Proposer on " + commonicator.getSelfEndpoint().toString()
				+ " started sending proposal...");
		Preparation preparation = new Preparation();
		preparation.setSequenceNumber(proposal.getSequenceNumber());
		preparation.setEps(proposal.getEps());
		commonicator.send(preparation);
		long maxAcceptedSeqenceNumber = Long.MIN_VALUE;
		for (Endpoint acceptor : preparation.getEps()) {
			try {
				Preparation response = (Preparation) commonicator.receive(
						Configuration.Communication.COMMUNICATE_TIMEOUT,
						Configuration.Communication.TIME_UNIT, acceptor);
				if (response == null) {
					logger.error("Read message failed from Endpoint["
							+ acceptor.toString() + "]");
					continue;
				}
				if (maxAcceptedSeqenceNumber < response.getSequenceNumber()) {
					maxAcceptedSeqenceNumber = response.getSequenceNumber();
				}
				logger.info("receive maxAcceptedSeqenceNumber:"
						+ maxAcceptedSeqenceNumber);
			} catch (Exception e) {
				logger.error("Receive response from " + acceptor.toString()
						+ " failed:" + e.getMessage());
				e.printStackTrace();
				return -1;
			}
		}
		return maxAcceptedSeqenceNumber;
	}

	protected abstract byte[] generateProposalContent();

	protected abstract long generateProposalSeqenceNumber();
}
