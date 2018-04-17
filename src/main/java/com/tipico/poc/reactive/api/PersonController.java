package com.tipico.poc.reactive.api;

import com.tipico.poc.reactive.model.Person;
import com.tipico.poc.reactive.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonRepository personRespository;

	@GetMapping
	public Flux<Person> index() {
		Flux<Person> persons = personRespository.findAll();
		// return persons.filter(p -> p.getName().startsWith("C")).map(p -> new Person(p.getId(), p.getName().toUpperCase()));
		return persons.map(p -> new Person(p.getId(), p.getName().toUpperCase()));
	}

	@GetMapping("/delay")
	public Flux<Person> delay() {
		Flux<Person> persons = personRespository.findAllWithDelay(10);
		Flux<Person> personsSecondBatch = personRespository.findAllWithDelay(1);
		Flux capitalPersons =  persons.map(p -> new Person(p.getId(), p.getName().toUpperCase()));
		Flux result = Flux.concat(capitalPersons, personsSecondBatch);
		return result;
	}
}