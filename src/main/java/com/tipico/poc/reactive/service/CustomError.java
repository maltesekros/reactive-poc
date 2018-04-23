package com.tipico.poc.reactive.service;

public final class CustomError extends Throwable {

	public CustomError(String message) {
		super(message);
	}
}
