package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonRepository {

	private List<Person> personDb;

	@PostConstruct
	private void init() {
		// Init DB with some dummy data
		personDb = new ArrayList<>();
		personDb.add(new Person(1, "Chris", "Micallef"));
		personDb.add(new Person(2, "Luke", "Micallef"));
		personDb.add(new Person(3, "Melanie", "Micallef"));
		personDb.add(new Person(4, "Samir", "Handanovic"));
		personDb.add(new Person(5, "Mauro", "Icardi"));
	}

	public Flux<Person> findAll() {
		return Flux.fromIterable(this.personDb);
	}

	public Flux<Person> findAllWithDelayOriginal(int delayInSeconds) {
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(delayInSeconds));
		Flux result = Flux.fromIterable(personDb).zipWith(delay, (s, l) -> s);
		System.out.printf("Return result after [delay: %d]%n", delayInSeconds);
		return result;
	}

	public Flux<Person> findAllWithDelay(int delayInSeconds) {
		Flux result = Flux.fromIterable(personDb).delayElements(Duration.ofSeconds(delayInSeconds));
		System.out.printf("Return result after [delay: %d]%n", delayInSeconds);
		return result;
	}
}
