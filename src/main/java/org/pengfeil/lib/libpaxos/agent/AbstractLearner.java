package org.pengfeil.lib.libpaxos.agent;

import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;
import org.pengfeil.lib.libpaxos.io.model.MessageType;
import org.pengfeil.lib.libpaxos.io.model.Notification;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

public abstract class AbstractLearner extends AbstractAgent implements ILearner {
	protected ProposalHandler handler;

	public AbstractLearner(Communicator commonicator) {
		super(commonicator);
	}
	
	public void start() {
		logger.info("Learner on " + commonicator.getSelfEndpoint().toString() + " started listening...");
		commonicator.listen(new MessageHandler());
	}
	
	protected abstract void recordAcceptedProposal(Proposal proposal, Endpoint acceptor);
	
	protected abstract void triggerHandler();
	
	public void registerHandler(ProposalHandler handler){
		this.handler = handler;
	}
	
	private class MessageHandler implements Communicator.ListenHandler {

		public void handle(Message msg, Endpoint from) {
			if(msg.getMessageType() == MessageType.NOTIFICATION){
				//Handler notification from acceptors
				Notification notification = (Notification) msg;
				Proposal acceptedProposal = notification.getAcceptedProposal();
				recordAcceptedProposal(acceptedProposal, from);
				triggerHandler();
			}
		}
		
	}
}
