package org.pengfeil.lib.libpaxos.agent;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pengfeil.lib.libpaxos.io.Communicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;

/**
 * Abstract class for defining common behaviors of all kinds of agents.
 * 
 */
public abstract class AbstractAgent {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected Communicator commonicator;

	protected List<Endpoint> acceptors = new LinkedList<Endpoint>();
	protected List<Endpoint> proposers = new LinkedList<Endpoint>();
	protected List<Endpoint> learners = new LinkedList<Endpoint>();

	public AbstractAgent(Communicator commonicator) {
		this.commonicator = commonicator;
	}

	public List<Endpoint> getAcceptors() {
		return acceptors;
	}

	public void setAcceptors(List<Endpoint> acceptors) {
		this.acceptors = acceptors;
	}

	public List<Endpoint> getProposers() {
		return proposers;
	}

	public void setProposers(List<Endpoint> proposers) {
		this.proposers = proposers;
	}

	public List<Endpoint> getLearners() {
		return learners;
	}

	public void setLearners(List<Endpoint> learners) {
		this.learners = learners;
	}

	protected Endpoint[] getMajority(List<Endpoint> endpoints) {
		int size = endpoints.size();
		int majoritySize = size / 2 + 1;
		Endpoint[] majorityAcceptors = new Endpoint[majoritySize];
		int startPoint = (int) System.currentTimeMillis() % size;
		for (int i = startPoint, count = 0; count < majoritySize; i++, count++) {
			majorityAcceptors[count] = endpoints.get(i % size);
		}
		return majorityAcceptors;
	}

}
