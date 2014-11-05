package org.pengfeil.lib.libpaxos.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.pengfeil.lib.libpaxos.io.model.Endpoint;
import org.pengfeil.lib.libpaxos.io.model.Message;

public class BlockingQueueChannel extends BlockingChannel {
	private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

	public BlockingQueueChannel(Endpoint from, Endpoint to) {
		super(from, to);
	}

	public Message read() {
		try {
			Message msg = messageQueue.take();
			logger.debug("read from messageQueue. now size:" + messageQueue.size());
			return msg;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean send(Message msg) {
		try {
			messageQueue.put(msg);
			logger.debug("send to messageQueue. now size:" + messageQueue.size());
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public long count() {
		return messageQueue.size();
	}
	
	
}
