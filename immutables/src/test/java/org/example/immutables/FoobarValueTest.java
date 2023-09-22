package org.example.immutables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoobarValueTest {

    @Test
    void create() {
        var a = ImmutableFoobarValue.builder()
                .foo(1)
                .build();
        var b = ImmutableFoobarValue.builder()
                .foo(1)
                .build();

        assertEquals(a, b);
    }
}