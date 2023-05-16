package org.example.async;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class MutinyStream {

    public static void main(String[] args) {
        Multi.createFrom().items(1, 2, 3, 4, 5)
                .onSubscription().invoke(() -> log.info("Start!"))
                .filter(i -> i != 5)
                .flatMap(i -> Uni.createFrom().item(i * i).onItem().delayIt().by(Duration.ofMillis(1000)).toMulti())
                .map(i -> {
                    if(i == 25) {
                        throw new RuntimeException("You shall not pass!");
                    }
                    return i;
                })
                .subscribe().with(i -> log.info("Event: {}", i),
                        throwable -> log.error("Error: {}", throwable.getMessage()),
                        () -> log.info("Finished"));
    }
}
