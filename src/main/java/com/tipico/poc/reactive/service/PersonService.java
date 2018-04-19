package com.tipico.poc.reactive.service;

import com.tipico.poc.reactive.dto.PersonDTO;
import com.tipico.poc.reactive.model.Person;
import com.tipico.poc.reactive.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public Flux<PersonDTO> getPersonById(int id) {
		Flux<PersonDTO> personResult = personRepository.findAll().filter(p -> p.getId() == id).map(p -> {
			PersonDTO personDTO = mapPersonToPersonDTO(p);
			return personDTO;
		});
		return personResult;
	}

	private PersonDTO mapPersonToPersonDTO(Person p) {
		PersonDTO personDTO = new PersonDTO();
		personDTO.setName(p.getName());
		personDTO.setSurname(p.getSurname());
		personDTO.setAge(p.getAge());
		return personDTO;
	}
}
