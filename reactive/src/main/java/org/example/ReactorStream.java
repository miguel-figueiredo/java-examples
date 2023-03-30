package org.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ReactorStream {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * i)
                .subscribe(i -> log.info("Event: {}", i));
    }
}
