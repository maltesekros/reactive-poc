package com.tipico.poc.reactive.service;

import com.tipico.poc.reactive.dto.PersonDTO;
import com.tipico.poc.reactive.model.Address;
import com.tipico.poc.reactive.model.Person;
import com.tipico.poc.reactive.repo.AddressRepository;
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
	@Autowired
	private AddressRepository addressRepository;

	public Mono<PersonDTO> getPersonById(int id) {
		Flux<PersonDTO> personResult = personRepository.findAll().filter(p -> p.getId() == id).map(p -> {
			logger.info(String.format("Found person with [id: %d]", id));
			PersonDTO personDTO = mapPersonToPersonDTO(p, null);
			return personDTO;
		}).switchIfEmpty(Mono.error(new CustomError("No element found with id. ")));
		return personResult.next();
	}

	public Mono<PersonDTO> getFullPersonDetailsById(int id) {
		Mono<Person> personMono = personRepository.findPersonById(id);
		Mono<Address> addressMono = addressRepository.findAddressByPersonId(id);
		Flux<PersonDTO> fullDetailsPersonDTO = Flux.zip(personMono, addressMono).map(t -> {
			PersonDTO personDTO = mapPersonToPersonDTO(t.getT1(), t.getT2());
			return personDTO;
		});
		return fullDetailsPersonDTO.next();
	}

	private PersonDTO mapPersonToPersonDTO(Person person, Address address) {
		PersonDTO personDTO = new PersonDTO();
		personDTO.setName(person.getName());
		personDTO.setSurname(person.getSurname());
		personDTO.setAge(person.getAge());
		if (address != null) {
			personDTO.setHouseName(address.getHouseName());
			personDTO.setStreet(address.getStreetName());
			personDTO.setCity(address.getCity());
		}
		return personDTO;
	}

	public Flux<PersonDTO> getAllPersons() {
		Flux<PersonDTO> allPersonsResult = personRepository.findAll().map(p -> {
			PersonDTO personDTO = mapPersonToPersonDTO(p, null);
			return personDTO;
		});
		return allPersonsResult;
	}

	public Flux<PersonDTO> getAllPersonsWithDelay(int secondsDelay) {
		Flux<PersonDTO> allPersonsResult = personRepository.findAllWithDelay(secondsDelay).map(p -> {
			PersonDTO personDTO = mapPersonToPersonDTO(p, null);
			return personDTO;
		});
		logger.info("Exit service getAllPersonsWithDelay [OK]");
		return allPersonsResult;
	}
}
