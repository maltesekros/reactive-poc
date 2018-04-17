package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class PersonRepository {

	public Flux<Person> findAll() {
		return Flux.just(new Person(1, "Chris"), new Person(2, "Luke"));
	}

	public Flux<Person> findAllWithDelayOriginal(int delayInSeconds) {
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(delayInSeconds));
		Flux result = Flux.just(new Person(1, "Chris"), new Person(2, "Melanie")).zipWith(delay, (s, l) -> s);
		System.out.printf("Return result after [delay: %d]%n", delayInSeconds);
		return result;
	}

	public Flux<Person> findAllWithDelay(int delayInSeconds) {
		Flux result = Flux.just(new Person(1, "Chris"), new Person(2, "Melanie"));
		try {
			Thread.sleep(delayInSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Return result after [delay: %d]%n", delayInSeconds);
		return result;
	}
}
