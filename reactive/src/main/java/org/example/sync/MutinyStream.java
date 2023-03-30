package org.example.sync;

import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutinyStream {

    public static void main(String[] args) {
        Multi.createFrom().items(1, 2, 3, 4, 5)
                .map(i -> i * i)
                .subscribe().with(i -> log.info("Event: {}", i));
    }
}
