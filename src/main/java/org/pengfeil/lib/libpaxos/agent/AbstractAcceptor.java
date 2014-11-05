package org.pengfeil.lib.libpaxos.agent;

import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;
import org.pengfeil.lib.libpaxos.io.model.MessageType;
import org.pengfeil.lib.libpaxos.io.model.Notification;
import org.pengfeil.lib.libpaxos.io.model.Preparation;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

public abstract class AbstractAcceptor extends AbstractAgent {

	protected long minGuaranteedSequenceNumber = Long.MIN_VALUE;

	public AbstractAcceptor(Communicator commonicator) {
		super(commonicator);
	}

	public void start() {
		logger.info("Acceptor on " + commonicator.getSelfEndpoint().toString() + " started listening...");
		commonicator.listen(new MessageHandler());
	}

	/**
	 * Get all accepted proposal. For producing preparation response. Subclass
	 * need to implement this with in memory or disk storage.
	 * 
	 * @return
	 */
	protected abstract Proposal[] getAcceptedProposals();

	/**
	 * Accept a proposal. Store to storage.
	 * 
	 * @param proposal
	 */
	protected abstract void acceptProposal(Proposal proposal);

	/**
	 * Currently, just notify all learners.
	 * 
	 * @param acceptedProposal
	 */
	protected void notifyLearners(Proposal acceptedProposal) {
		Notification notification = new Notification();
		notification.setAcceptedProposal(acceptedProposal);
		notification.setEps(learners.toArray(new Endpoint[0]));
		commonicator.send(notification);
	}

	private class MessageHandler implements Communicator.ListenHandler {
		public void handle(Message msg, Endpoint from) {
			if (msg.getMessageType() == MessageType.PREPARATION) {
				// Handle preparation message
				Preparation preparation = (Preparation) msg;
				Preparation response = new Preparation();
				response.setEps(new Endpoint[] { from });
				// Guarantee to accept non small sequence number than
				// minGuaranteedSequenceNumber
				if (minGuaranteedSequenceNumber <= preparation
						.getSequenceNumber()) {
					response.setSequenceNumber(preparation.getSequenceNumber());
					minGuaranteedSequenceNumber = preparation
							.getSequenceNumber();
					logger.info("Guarantee to accept non small sequence number than "
							+ minGuaranteedSequenceNumber);
				} else {
					response.setSequenceNumber(minGuaranteedSequenceNumber);
				}
				commonicator.send(response);
			} else if (msg.getMessageType() == MessageType.PROPOSAL) {
				// Handle proposal message
				Proposal proposal = (Proposal) msg;
				if (minGuaranteedSequenceNumber <= proposal.getSequenceNumber()) {
					acceptProposal(proposal);
					logger.info("Accept the proposal " + proposal.toString());
					notifyLearners(proposal);
				} else {
					// Else just drop the proposal
					logger.info("Drop the proposal " + proposal.toString());
				}
			}
		}
	}
}
