package com.tipico.poc.reactive.api;

import java.util.List;

public interface MyEventListener<T> {

	void onDataChunk(List<T> chunk);

	void processComplete();
}
