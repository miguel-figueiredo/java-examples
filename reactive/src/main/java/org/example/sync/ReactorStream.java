package org.example.sync;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class ReactorStream {

    public static void main(String[] args) throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15)
                .doOnSubscribe(i -> log.info("Started"))
                .filter(i -> i != 5)
                .flatMap(i -> Mono.just(i * i).delayElement(Duration.ofMillis(1000)))
                .map(i -> {
                    if(i == 25) {
                        throw new RuntimeException("You shall not pass!");
                    }
                    return i;
                })
                .subscribe(i -> log.info("Event: {}", i),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("Finished"));

        Thread.currentThread().join();
    }
}
