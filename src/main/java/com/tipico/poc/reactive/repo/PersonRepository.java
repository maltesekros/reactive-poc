package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonRepository {

	private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
	private List<Person> personDb;

	@PostConstruct
	private void init() {
		// Init DB with some dummy data
		personDb = new ArrayList<>();
		personDb.add(new Person(1, "Chris", "Micallef", 40));
		personDb.add(new Person(2, "Luke", "Micallef", 5));
		personDb.add(new Person(3, "Melanie", "Micallef", 34));
		personDb.add(new Person(4, "Samir", "Handanovic", 34));
		personDb.add(new Person(5, "Mauro", "Icardi", 25));
	}

	public Flux<Person> findAll() {
		return Flux.fromIterable(this.personDb);
	}

	public Flux<Person> findAllWithDelay(int delayInSeconds) {
		Flux result = Flux.fromIterable(personDb).delayElements(Duration.ofSeconds(delayInSeconds));
		logger.info(String.format("Return result after [delay: %d] [OK]", delayInSeconds));
		return result;
	}

	public Flux<Person> findAllWithDelayV2(int delayInSeconds) {
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(delayInSeconds));
		Flux result = Flux.fromIterable(personDb).zipWith(delay, (s, l) -> s);
		logger.info(String.format("Return result after [delay: %d] [OK]", delayInSeconds));
		return result;
	}
}
