package com.tipico.poc.reactive.service;

import com.tipico.poc.reactive.dto.PersonDTO;
import com.tipico.poc.reactive.model.Person;
import com.tipico.poc.reactive.repo.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
	@Autowired
	private PersonRepository personRepository;

	public Mono<PersonDTO> getPersonById(int id) {
		Flux<PersonDTO> personResult = personRepository.findAll().filter(p -> p.getId() == id).map(p -> {
			logger.info(String.format("Found person with [id: %d]", id));
			PersonDTO personDTO = mapPersonToPersonDTO(p);
			return personDTO;
		}).switchIfEmpty(Mono.error(new CustomError("No element found with id. ")));
		return personResult.next();
	}

	private PersonDTO mapPersonToPersonDTO(Person p) {
		PersonDTO personDTO = new PersonDTO();
		personDTO.setName(p.getName());
		personDTO.setSurname(p.getSurname());
		personDTO.setAge(p.getAge());
		return personDTO;
	}

	public Flux<PersonDTO> getAllPersons() {
		Flux<PersonDTO> allPersonsResult = personRepository.findAll().map(p -> {
			PersonDTO personDTO = mapPersonToPersonDTO(p);
			return personDTO;
		});
		return allPersonsResult;
	}

	public Flux<PersonDTO> getAllPersonsWithDelay(int secondsDelay) {
		Flux<PersonDTO> allPersonsResult = personRepository.findAllWithDelay(secondsDelay).map(p -> {
			PersonDTO personDTO = mapPersonToPersonDTO(p);
			return personDTO;
		});
		logger.info("Exit service getAllPersonsWithDelay [OK]");
		return allPersonsResult;
	}
}
