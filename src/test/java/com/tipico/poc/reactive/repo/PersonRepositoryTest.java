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
}
