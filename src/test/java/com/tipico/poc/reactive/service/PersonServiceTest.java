package com.tipico.poc.reactive.service;

import com.tipico.poc.reactive.dto.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
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
		Flux<PersonDTO> personFlux = personService.getPersonById(1);
		// Assert ...
		StepVerifier.create(personFlux).recordWith(ArrayList::new).
			expectNextCount(1).consumeRecordedWith(results -> {
				PersonDTO p = results.iterator().next();
				assertEquals("Chris", p.getName());
		}).verifyComplete();
	}
}
