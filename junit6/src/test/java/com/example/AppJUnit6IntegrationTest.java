package com.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration-test")
public class AppJUnit6IntegrationTest {
    @Test
    void integrationTestExample() {
        // Simulate integration test logic
        org.junit.jupiter.api.Assertions.assertTrue(true, "JUnit 6 integration test with tag runs");
    }
}
