package com.tipico.poc.reactive.repo;

import com.tipico.poc.reactive.model.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressRepositoryTest {

	@Autowired
	AddressRepository addressRepository;

	@Test
	public void testFindAddressByPersonId() {
		Mono<Address> address = addressRepository.findAddressByPersonId(1);
		address.subscribe(a -> System.out.println(a));

		Mono<Address> address2 = addressRepository.findAddressByPersonId(155678);
		StepVerifier.create(address2).expectError().verifyThenAssertThat().hasOperatorErrorWithMessage("No element found with personId. ");
	}
}
