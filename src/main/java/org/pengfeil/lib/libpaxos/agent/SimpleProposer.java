package org.pengfeil.lib.libpaxos.agent;

import org.pengfeil.lib.libpaxos.io.Communicator;

public class SimpleProposer extends AbstractProposer implements IProposer {

	public SimpleProposer(Communicator commonicator) {
		super(commonicator);
	}

	@Override
	protected byte[] generateProposalContent() {
		return (System.currentTimeMillis() + Thread.currentThread().toString()).getBytes();
	}

	@Override
	protected long generateProposalSeqenceNumber() {
		return System.currentTimeMillis();
	}
}
