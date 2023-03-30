package org.example.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.Failable;

import java.util.stream.IntStream;

@Slf4j
public class JavaStream {

    public static void main(String[] args) {
        IntStream.range(0, 20).boxed()
                .map(Failable.asFunction(i -> {
                    Thread.sleep(1000);
                    return i * i;
                }))
                .forEach(i -> log.info("Event: {}", i));
    }
}
