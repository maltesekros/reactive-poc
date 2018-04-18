package com.tipico.poc.reactive.api;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;

public class SampleSubscriber<T> extends BaseSubscriber<T> {

	private static final Logger logger = LoggerFactory.getLogger(SampleSubscriber.class);

	public void hookOnSubscribe(Subscription subscription) {
		logger.info("Subscribed");
		// Propagate backpressure and request exactly one element from the source
		request(1);
	}

	public void hookOnNext(T value) {
		logger.info(value.toString());
		request(1);
	}
}