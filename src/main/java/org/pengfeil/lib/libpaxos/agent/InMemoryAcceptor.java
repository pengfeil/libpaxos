package org.pengfeil.lib.libpaxos.agent;

import java.util.LinkedList;
import java.util.List;

import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

public class InMemoryAcceptor extends AbstractAcceptor {
	
	private List<Proposal> acceptedProposal = new LinkedList<Proposal>();

	public InMemoryAcceptor(Communicator commonicator) {
		super(commonicator);
	}

	@Override
	public Proposal[] getAcceptedProposals() {
		return acceptedProposal.toArray(new Proposal[0]);
	}

	@Override
	public void acceptProposal(Proposal proposal) {
		acceptedProposal.add(proposal);
	}

}
