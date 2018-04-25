package com.tipico.poc.reactive.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleSupplier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveDemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void zipping() {
		Flux<String> titles = Flux.just("Mr.", "Mrs.");
		Flux<String> firstNames = Flux.just("John", "Jane");
		Flux<String> lastNames = Flux.just("Doe", "Blake");
		Flux<String> names = Flux
			.zip(titles, firstNames, lastNames)
			.map(t -> t.getT1() + " " + t.getT2() + " " + t.getT3());
		StepVerifier.create(names).expectNext("Mr. John Doe", "Mrs. Jane Blake").verifyComplete();

		Flux<Long> delay = Flux.interval(Duration.ofMillis(5));
		Flux<String> firstNamesWithDelay = firstNames.zipWith(delay, (s, l) -> s);
		Flux<String> namesWithDelay = Flux
			.zip(titles, firstNamesWithDelay, lastNames)
			.map(t -> t.getT1() + " " + t.getT2() + " " + t.getT3());
		StepVerifier.create(namesWithDelay).expectNext("Mr. John Doe", "Mrs. Jane Blake").verifyComplete();
	}

	@Test
	public void interleave() {
		Flux<Long> delay = Flux.interval(Duration.ofMillis(5));
		Flux<String> alphabetsWithDelay = Flux.just("A", "B").zipWith(delay, (s, l) -> s);
		Flux<String> alphabetsWithoutDelay = Flux.just("C", "D");
		Flux<String> interleavedFlux = alphabetsWithDelay.mergeWith(alphabetsWithoutDelay);
		StepVerifier.create(interleavedFlux).expectNext("C", "D", "A", "B").verifyComplete();
		Flux<String> nonInterleavedFlux = alphabetsWithDelay.concatWith(alphabetsWithoutDelay);
		StepVerifier.create(nonInterleavedFlux).expectNext("A", "B", "C", "D").verifyComplete();
	}

	@Test(expected = AssertionError.class)
	public void simpleTest() {
		Mono<String> someData = Mono.empty();
		// We will just see done printed wince we are dealing with an empty Mono
		someData.subscribe(str -> System.out.println(str), error -> System.out.println("Error!"), () -> System.out.println("Done"));

		// Expected to work
		Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);
		StepVerifier.create(numbersFromFiveToSeven).expectNext(5, 6, 7).verifyComplete();

		// Expected to fail since onComplete will be called not onNext
		StepVerifier.create(numbersFromFiveToSeven).expectNext(5, 6, 7, 8).verifyComplete();
	}

	@Test
	public void testFlux() {
		Flux<Integer> ints = Flux.range(1, 10).map(i -> {
			if (i == 4) {
				i *= 2;
			}
			else if (i == 8) {
				throw new RuntimeException("Error!!");
			}
			return i;
			});
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));
	}

	@Test
	public void testFlux2() {
		Flux<Integer> ints = Flux.range(1, 10).map(i -> {
			if (i == 4) {
				i *= 2;
			}
			else if (i == 7) {
				throw new RuntimeException("Error!!");
			}
			return i;
		});
		// 'Done' will never be printed since we will have an error before
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error), () -> System.out
			.println("Done"));
	}

	@Test
	public void testFlux3() {
		Flux<Integer> ints = Flux.range(1, 10).map(i -> {
			if (i == 4) {
				i *= 2;
			}
			return i;
		});
		// 'Done' will never be printed.
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error), () -> System.out
			.println("Done"));
	}

	@Test
	public void testFlux4() {
		Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
		List<String> list = Arrays.asList("foo", "bar", "foobar");

		Flux seq2 = Flux.fromIterable(list);

		seq1.subscribe(str -> System.out.println(str));
		seq2.subscribe(str -> System.out.println(str));
	}

	@Test
	public void testFlux5() {
		AsteriskAppender<Integer> asteriskAppender = new AsteriskAppender<Integer>();
		Flux<Integer> ints = Flux.range(1, 3);
		// Add a subscriber 'asteriskAppender' as a consumer
		ints.subscribe(i -> System.out.println(i),
			error -> System.err.println("Error: " + error),
			() -> System.out.println("Done"),
			s -> asteriskAppender.request(10));
		ints.subscribe(asteriskAppender);
	}

	@Test
	public void testFlux6() {
		// Programatically create the events (onNext, onError, onComplete)
		Flux<String> flux = Flux.generate(
			() -> 0, // Initial State
			(state, sink) -> {
				sink.next("3 x " + state + " = " + 3*state); // Set what to emit
				if (state == 10) sink.complete(); // When to stop
				return state + 1; // Return a new state
			});
		flux.subscribe(i -> System.out.println(i));
	}

	@Test
	public void testFlux7() {
		// Programatically create the events (onNext, onError, onComplete)
		Flux<String> flux = Flux.generate(
			AtomicLong::new, // Initial State
			(state, sink) -> {
				long i = state.getAndIncrement();
				sink.next("3 x " + i + " = " + 3*i); // Set what to emit
				if (i == 10) sink.complete(); // When to stop
				return state; // Return a new state
			}, state -> {
					System.out.println("Closing state: " + state);
					state = new AtomicLong(0);
					System.out.println("Cleanup: " + state);
				}
			); // Variant to clean up last state
		flux.subscribe(i -> System.out.println(i));
	}


	@Test
	public void testFlux8() {
		Flux<Double> flux = Flux.generate(
			() -> 0, // Initial State
			(state, sink) -> {
				DoubleSupplier sup = MyRandomNoGenerator::getRandom; // Mutate the state
				sink.next(sup.getAsDouble());
				if (state == 10) sink.complete(); // When to stop
				return state + 1; // Return a new state
			});
		flux.subscribe(i -> System.out.println(i));
	}

	@Test
	public void testFlux9() {
		// We have an event processor that published 'chunks' of data to a number of listeners
		MyEventProcessor myEventProcessor = new MyEventProcessor();
		// We will use the Flux.create to bridge a synchronous call to a Flux
		Flux<String> bridge = Flux.create(sink -> {
			myEventProcessor.register(
				new MyEventListener<String>() {
					public void onDataChunk(List<String> chunk) {
						for(String s : chunk) {
							sink.next(s);
						}
					}

					public void processComplete() {
						sink.complete();
					}
				});
		});
		bridge.subscribe(s -> System.out.println("Received Flux: " + s), error -> System.err
			.println("Error!"), () -> System.out.println("Done!!"));
		myEventProcessor.publish(Arrays.asList("Foo", "Bar"));
		try {
			Thread.sleep(4000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myEventProcessor.publish(Arrays.asList("FooBar"));
		myEventProcessor.complete();
	}

	private String alphabet(int letterNumber) {
		if (letterNumber < 1 || letterNumber > 26) {
			return null;
		}
		int letterIndexAscii = 'A' + letterNumber - 1;
		return "" + (char) letterIndexAscii;
	}

	@Test
	public void testFlux10() {
		// Testing .handle operator
		Flux<String> alphabetFlux = Flux.just(-1, 30, 13, 9, 20)
			.handle((i, sink) -> {
				// Sink will filter out any NULLs returned
				String letter = alphabet(i);
				if (letter != null) {
					sink.next(letter);
				}
			});
		alphabetFlux.subscribe(System.out::println);
	}

	@Test
	public void testFlux11() {
		Flux intervalFlux = Flux.interval(Duration.ofMillis(1000), Schedulers.newSingle("test"))
			.handle((item, sink) -> {
				sink.next(item);
			});
		intervalFlux.subscribe(new SampleSubscriber());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Flux intervalFlux2 = Flux.interval(Duration.ofMillis(1000), Schedulers.parallel())
			.handle((item, sink) -> {
				sink.next(item);
			});
		intervalFlux2.subscribe(new SampleSubscriber());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testColdSource() {
		Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
			.doOnNext(System.out::println)
			.filter(s -> s.startsWith("o"))
			.map(String::toUpperCase);
		// All subscribers will ALL the items
		source.subscribe(colour -> System.out.println("Subscriber 1: " + colour));
		source.subscribe(colour -> System.out.println("Subscriber 2: " + colour));
		source.subscribe(colour -> System.out.println("Subscriber 3: " + colour));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHotSource() {
		UnicastProcessor<String> hotSource = UnicastProcessor.create();
		Flux<String> hotFlux = hotSource.publish()
			.autoConnect()
			.map(String::toUpperCase);
		hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));
		hotSource.onNext("blue");
		hotSource.onNext("green");
		// The second subscriber will NOT get all the items but will start getting only
		// those from when he subsribed.
		hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));
		hotSource.onNext("orange");
		hotSource.onNext("purple");
		hotSource.onComplete();
	}

}

class MyRandomNoGenerator {
	public static Double getRandom(){
		return Math.random();
	}
}
