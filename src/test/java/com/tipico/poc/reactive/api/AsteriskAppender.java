package com.tipico.poc.reactive.api;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class AsteriskAppender<Integer> extends BaseSubscriber<Integer> {

	public void hookOnNext(Integer value) {
		// Propagate backpressure and request exactly one element from the source
		System.out.println("* * " + value + " * *");
		request(1);
	}

	public void hookOnSubscribe(Subscription subscription) {
		System.out.println("Subscribed");
		// We need to request at least to request 1 element so that the Flux will NOT
		// get stuck.
		request(1);
	}
}
