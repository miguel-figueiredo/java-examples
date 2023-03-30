package org.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class JavaStream {

    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4, 5)
                .map(i -> i * i)
                .forEach(i -> log.info("Event: {}", i));
    }
}
