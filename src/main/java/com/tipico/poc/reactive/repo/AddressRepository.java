package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Address;
import com.tipico.poc.reactive.service.CustomError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddressRepository {

	private static final Logger logger = LoggerFactory.getLogger(AddressRepository.class);
	private List<Address> addressDb;

	@PostConstruct
	private void init() {
		// Init DB with some dummy data
		addressDb = new ArrayList<>();
		addressDb.add(new Address(10, 1, "Arcade Fire", "Funeral", "Toronto"));
		addressDb.add(new Address(20, 2, "Oasis", "What's The Story", "Manchester"));
		addressDb.add(new Address(30, 3, "Beatles", "Penny Lane", "Liverpool"));
		addressDb.add(new Address(40, 4, "Cranberries", "No Need to Argue", "Limerick"));
		addressDb.add(new Address(50, 5, "The National", "High Violet", "Cincinati"));
	}

	public Flux<Address> findAll() {
		return Flux.fromIterable(this.addressDb);
	}

	public Flux<Address> findAllWithDelay(int delayInSeconds) {
		Flux result = Flux.fromIterable(this.addressDb).delayElements(Duration.ofSeconds(delayInSeconds));
		logger.info(String.format("Return result after [delay: %d] [OK]", delayInSeconds));
		return result;
	}

	public Mono<Address> findAddressByPersonId(int personId) {
		Mono<Address> result = findAll().filter(a -> a.getPersonId() == personId).switchIfEmpty(Mono.error(new CustomError("No element found with personId. "))).next();
		return result;
	}

}
