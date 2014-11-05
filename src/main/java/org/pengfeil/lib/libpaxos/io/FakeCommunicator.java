package org.pengfeil.lib.libpaxos.io;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;

/**
 * Fake network for testing. All agents should run on same machine.
 * 
 */
public class FakeCommunicator extends Communicator {
	/**
	 * Key for channel map
	 * 
	 */
	private class Pair {
		private Endpoint from;
		private Endpoint to;

		public Pair(Endpoint from, Endpoint to) {
			super();
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}
	}

	// Should be <Endpoint, Channel> in real network implementation
	private static Map<Pair, Channel> network = new HashMap<Pair, Channel>();

	public FakeCommunicator(Endpoint selfEndpoint) {
		super(selfEndpoint);
	}

	public Message receive(long timeout, TimeUnit unit, final Endpoint endpoint)
			throws Exception {
		FutureTask<Message> task = new FutureTask<Message>(
				new Callable<Message>() {
					public Message call() throws Exception {
						Channel channel = network.get(new Pair(endpoint,
								selfEndpoint));
						if (channel == null) {
							logger.error("Can not find a channel with endpoint["
									+ endpoint.toString() + "]");
							return null;
						} else {
							logger.debug("Reading message from " + channel);
							return channel.read();
						}
					}
				});
		new Thread(task).start();
		return task.get(timeout, unit);
	}

	public void send(Message msg) {
		Endpoint[] eps = msg.getEps();
		for (Endpoint ep : eps) {
			Pair key = new Pair(selfEndpoint, ep);
			synchronized (network) {
				if (network.containsKey(key)) {
					Channel channel = network.get(key);
					channel.send(msg);
					logger.debug("Message has been sent to " + channel);
				} else {
					// Create two-way channels. Just like what TCP will do.
					BlockingQueueChannel channel = new BlockingQueueChannel(
							selfEndpoint, ep);
					// A to B channel
					network.put(key, channel);
					// B to A channel
					network.put(new Pair(ep, selfEndpoint),
							new BlockingQueueChannel(ep, selfEndpoint));
					logger.debug("Create channels between endpoints: ["
							+ selfEndpoint + "] to [" + ep + "]");
					channel.send(msg);
					logger.debug("Message has been sent to " + channel);
				}
			}
		}
	}

	@Override
	public void listen(ListenHandler handler) {
		while (true) {
			Set<Pair> keys = new HashSet<Pair>();
			
			//Get a snapshot
			synchronized (network) {
				keys.addAll(network.keySet());
			}
			for (Pair pair : keys) {
				// Find a income channel
				if (pair.to.equals(selfEndpoint)) {
					Channel incomeChannel = network.get(pair);
					if (incomeChannel != null && incomeChannel.count() > 0) {
						Message msg = incomeChannel.read();
						logger.debug("accepted income message from "
								+ incomeChannel);
						handler.handle(msg, incomeChannel.from);
					}
				}
			}
			// Just for trial, so hard code the interval
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
