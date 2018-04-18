package com.tipico.poc.reactive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class MyEventProcessor {

	private static Logger logger = LoggerFactory.getLogger(DemoController.class);
	private List<MyEventListener> listeners = new ArrayList<>();
	private List<String> backlogQueue = new ArrayList<>();

	public void register(MyEventListener myEventListener) {
		listeners.add(myEventListener);
	}

	public void publish(List<String> data) {
		for (MyEventListener myEventListener : listeners) {
			myEventListener.onDataChunk(data);
		}
	}

	public void complete() {
		listeners.forEach(MyEventListener::processComplete);
	}

	public void addDataToBacklogQueue(List<String> data) {
		this.backlogQueue.addAll(data);
	}

	public List<String> request(long n) {
		if (this.backlogQueue.isEmpty()) {
			logger.info("Requested Data with PULL [NULL]");
			return new ArrayList<>();
		} else {
			List<String> backlogQueueCopy = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				backlogQueueCopy.add(this.backlogQueue.remove(0));
			}
			logger.info(String.format("Requested Data with PULL %s [no: %d]", backlogQueueCopy, n));
			return backlogQueueCopy;
		}
	}
}
