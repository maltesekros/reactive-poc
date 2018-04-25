package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void firstTest() {
        Flux<Person> result = personRepository.findAllWithDelay(2);
        result.subscribe(i -> System.out.println(System.currentTimeMillis() + " -> " +  i.getName()), error -> System.err.println("Error: " + error), () -> System.out
                .println("Done"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTwoSubscribers() {
        // We will see the persons obtained from the second call before even though they
        // are called after since the ones in the first call are with delay.
        Flux<Person> delayPersonFlux = personRepository.findAllWithDelay(2);
        delayPersonFlux.subscribe(person -> System.out.println("1 > " + person.getName()));
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> System.out.println("2 > " + person.getName()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTwoSubscribers2() {
        Flux<Person> delayPersonFlux = personRepository.findAllWithDelay(2);
        Flux<Person> personFlux = Flux.just(new Person(1, "Fast", "Bob", 21), new Person(2, "Fast", "Jane", 33), new Person(3, "Fast", "Jack", 21));
        personFlux.concatWith(delayPersonFlux).subscribe(person -> System.out.println(person.getName() + " " + person.getSurname()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
