package org.pengfeil.lib.libpaxos.agent;

import org.pengfeil.lib.libpaxos.io.model.Proposal;

/**
 * Interface for defining Learner API
 *
 */
public interface ILearner {
	/**
	 * register handler when the final accepted proposal is determined.
	 * @return
	 */
	public void registerHandler(ProposalHandler handler);
	
	public static interface ProposalHandler{
		public void handle(Proposal proposal);
	}
}
