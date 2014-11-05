package org.pengfeil.lib.libpaxos.agent;

import java.util.HashMap;

import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

public class InMemoryLearner extends AbstractLearner {
	private HashMap<Endpoint, Proposal> records = new HashMap<Endpoint, Proposal>();
	private HashMap<Proposal, Long> acceptCount = new HashMap<Proposal, Long>();

	public InMemoryLearner(Communicator commonicator) {
		super(commonicator);
	}

	@Override
	protected void recordAcceptedProposal(Proposal proposal, Endpoint acceptor) {
		records.put(acceptor, proposal);
		if (acceptCount.containsKey(proposal)) {
			acceptCount
					.put(proposal, acceptCount.get(proposal).longValue() + 1);
		} else {
			acceptCount.put(proposal, 1L);
		}
	}
	@Override
	protected void triggerHandler() {
		for(Proposal proposal: acceptCount.keySet()){
			if(acceptCount.get(proposal).longValue() >= acceptors.size() / 2 + 1){
				handler.handle(proposal);
				//Just forget old record for trial
				records.clear();
				acceptCount.clear();
				return;
			}
		}
	}

}
