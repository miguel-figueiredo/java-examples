package org.example.async;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.IntStream;

@Slf4j
public class ReactorStream {

    public static void main(String[] args) throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5)
                .doOnSubscribe(subscription -> log.info("Start!"))
                .filter(i -> i != 5)
                .flatMap(i -> Mono.just(i * i).delayElement(Duration.ofMillis(1000)))
                .map(i -> {
                    if(i == 25) {
                        throw new RuntimeException("You shall not pass!");
                    }
                    return i;
                })
                .subscribe(i -> log.info("Event: {}", i),
                  error -> log.error("Error: {}", error.getMessage()),
                        () -> log.info("Finished"));

        Thread.currentThread().join();
    }
}
