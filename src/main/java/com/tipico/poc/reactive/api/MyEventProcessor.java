package com.tipico.poc.reactive.api;

import java.util.ArrayList;
import java.util.List;

class MyEventProcessor {
	List<MyEventListener> listeners = new ArrayList<>();

	public void register(MyEventListener myEventListener) {
		listeners.add(myEventListener);
	}

	public void publish(List<String> data) {
		for (MyEventListener myEventListener : listeners) {
			myEventListener.onDataChunk(data);
		}
	}

	public void complete() {
		for (MyEventListener myEventListener : listeners) {
			myEventListener.processComplete();
		}
	}
}
