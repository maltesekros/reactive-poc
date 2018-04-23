package com.tipico.poc.reactive.api;

import com.tipico.poc.reactive.dto.PersonDTO;
import com.tipico.poc.reactive.service.PersonService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/persons")
public class PersonController {

	private static final Logger logger = Logger.getLogger(PersonController.class);
	@Autowired
	private PersonService personService;

	@GetMapping
	public Flux<PersonDTO> index() {
		Flux<PersonDTO> persons = personService.getAllPersons();
		// return persons.filter(p -> p.getName().startsWith("C")).map(p -> new Person(p.getId(), p.getName().toUpperCase()));
		return persons.map(person -> {
			person.setName(person.getName().toUpperCase());
			person.setSurname(person.getSurname().toUpperCase());
			return person;
		});
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<PersonDTO>> findPersonById(@PathVariable int id) {
		logger.info(String.format("Called findPersonById with [id: %d]", id));
		return personService.getPersonById(id).map(
			ResponseEntity::ok).doOnError(e ->
			logger.error("Error: ", e))
				.onErrorReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/delay")
	public Flux<PersonDTO> findAllDelay() {
		Flux<PersonDTO> persons = personService.getAllPersonsWithDelay(2);
		Flux<PersonDTO> returnResult = persons.map(person -> {
			person.setName(person.getName().toUpperCase());
			person.setSurname(person.getSurname().toUpperCase());
			return person;
		});
		logger.info("Exit Controller [OK]");
		return returnResult;
	}

}