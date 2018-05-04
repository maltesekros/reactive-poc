package com.tipico.poc.reactive.service;

import com.tipico.poc.reactive.dto.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

	@Autowired
	PersonService personService;

	@Test
	public void testGetById() {
		Mono<PersonDTO> personFlux = personService.getPersonById(1);
		// Assert ...
		StepVerifier.create(personFlux).recordWith(ArrayList::new).
			expectNextCount(1).consumeRecordedWith(results -> {
				PersonDTO p = results.iterator().next();
				assertEquals("Chris", p.getName());
		}).verifyComplete();
	}

	@Test
	public void testGetByIdNonExistent() {
		// No person is found with this id.
		Mono<PersonDTO> personFlux = personService.getPersonById(1523543);
		// Assert ...
		StepVerifier.create(personFlux).expectErrorMessage("No element found with id. ").verify();
	}

	@Test
	public void testGetFullPersonDetailsById() {
		// No person is found with this id.
		Mono<PersonDTO> personFlux = personService.getFullPersonDetailsById(1);
		personFlux.subscribe(p -> System.out.println(p));
	}
}
