package org.pengfeil.lib.libpaxos.demo;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.pengfeil.lib.libpaxos.agent.ILearner.ProposalHandler;
import org.pengfeil.lib.libpaxos.agent.InMemoryAcceptor;
import org.pengfeil.lib.libpaxos.agent.InMemoryLearner;
import org.pengfeil.lib.libpaxos.agent.SimpleProposer;
import org.pengfeil.lib.libpaxos.io.FakeCommunicator;
import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Proposal;

public class Main {
	final static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		ConsoleAppender appender = new ConsoleAppender();
		appender.setName("ConsoleAppender");
		appender.setLayout(new PatternLayout("[%d][%t][%p]%C{1}:%m\r\n"));
		appender.setWriter(new PrintWriter(System.out));
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getRootLogger().addAppender(appender);

		List<Endpoint> acceptors = new LinkedList<Endpoint>() {
			{
				add(new Endpoint("acceptor1", 1));
				add(new Endpoint("acceptor2", 1));
				add(new Endpoint("acceptor3", 1));
				add(new Endpoint("acceptor4", 1));
				add(new Endpoint("acceptor5", 1));
			}
		};
		List<Endpoint> proposers = new LinkedList<Endpoint>() {
			{
				add(new Endpoint("proposer1", 1));
				add(new Endpoint("proposer2", 1));
				add(new Endpoint("proposer3", 1));
				add(new Endpoint("proposer4", 1));
				add(new Endpoint("proposer5", 1));
				add(new Endpoint("proposer6", 1));
			}
		};
		List<Endpoint> learners = new LinkedList<Endpoint>() {
			{
				add(new Endpoint("learner1", 1));
				add(new Endpoint("learner2", 1));
				add(new Endpoint("learner3", 1));
				add(new Endpoint("learner4", 1));
				add(new Endpoint("learner5", 1));
			}
		};

		for (Endpoint ep : acceptors) {
			InMemoryAcceptor acceptor = new InMemoryAcceptor(
					new FakeCommunicator(ep));
			acceptor.setAcceptors(acceptors);
			acceptor.setLearners(learners);
			acceptor.setProposers(proposers);
			new Thread(new AcceptorThread(acceptor)).start();
		}

		for (Endpoint ep : learners) {
			InMemoryLearner learner = new InMemoryLearner(new FakeCommunicator(
					ep));
			learner.setAcceptors(acceptors);
			learner.setLearners(learners);
			learner.setProposers(proposers);
			new Thread(new LearnerThread(learner)).start();
		}

		for (Endpoint ep : proposers) {
			SimpleProposer proposer = new SimpleProposer(new FakeCommunicator(
					ep));
			proposer.setAcceptors(acceptors);
			proposer.setLearners(learners);
			proposer.setProposers(proposers);
			new Thread(new ProposerThread(proposer)).start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	public static class AcceptorThread implements Runnable {
		private InMemoryAcceptor acceptor;

		public AcceptorThread(InMemoryAcceptor acceptor) {
			super();
			this.acceptor = acceptor;
		}

		public void run() {
			acceptor.start();
		}
	}

	public static class ProposerThread implements Runnable {
		private SimpleProposer proposer;

		public ProposerThread(SimpleProposer proposer) {
			super();
			this.proposer = proposer;
		}

		public void run() {
//			while (true) {
				proposer.propose();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
//			}
		}
	}

	public static class LearnerThread implements Runnable {
		private InMemoryLearner learner;

		public LearnerThread(InMemoryLearner learner) {
			super();
			this.learner = learner;
		}

		public void run() {
			learner.registerHandler(new ProposalHandler() {
				public void handle(Proposal proposal) {
					System.out.println(Thread.currentThread() + " get "
							+ proposal.toString());
				}
			});
			learner.start();
		}
	}
}
