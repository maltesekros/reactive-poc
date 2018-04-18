package com.tipico.poc.reactive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Controller
public class DemoController {

	private static Logger logger = LoggerFactory.getLogger(DemoController.class);
	private MyEventProcessor myEventProcessor = new MyEventProcessor();

	@GetMapping("/hello-world")
	@ResponseBody
	public Mono<String> sayHello(HttpServletRequest request) {
		return Mono.just("Hello World");
	}

	@GetMapping("/hello-flux")
	@ResponseBody
	public Flux<String> fluxHello(HttpServletRequest request) {
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(5));
		Flux<String> alphabetsWithDelay = Flux.just("Hello", "Flux").zipWith(delay, (s, l) -> s);
		return alphabetsWithDelay;
	}

	@GetMapping("/bridge")
	@ResponseBody
	public Flux<String> bridge(HttpServletRequest request) {
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
		return bridge;
	}

	@GetMapping("/hybridBridge")
	@ResponseBody
	public Flux<String> hybridBridge(HttpServletRequest request) {
		// We will use the Flux.create to bridge a synchronous call to a Flux
		Flux<String> bridge = Flux.create(sink -> {
			myEventProcessor.register(
				new MyEventListener<String>() {
					public void onDataChunk(List<String> chunk) {
						for(String s : chunk) {
							logger.info("Serving data with PUSH");
							sink.next(s);
//							try {
//								Thread.sleep(5000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
							logger.info("Serving data with PUSH [OK]");
						}
					}
					public void processComplete() {
						sink.complete();
					}
				});
			sink.onRequest(n -> {
				List<String> messages = myEventProcessor.request(n);
				for(String s : messages) {
					sink.next(s);
					logger.info("Serving data with PULL");
				}
			});
		});
		return bridge;
	}

	@GetMapping("/publish/{data}")
	@ResponseBody
	public String publishData(@PathVariable String data) {
		logger.info(String.format("Received data to publish [data: %s]", data));
		myEventProcessor.addDataToBacklogQueue(Arrays.asList("Hello", "World"));
		myEventProcessor.publish(Arrays.asList(data));
		return "OK";
	}

	@GetMapping("/close")
	@ResponseBody
	public String closeFlux() {
		logger.info("Received signal to close Flux. ");
		myEventProcessor.complete();
		return "OK";
	}
}
