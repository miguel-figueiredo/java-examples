package com.example;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class AppJUnit4IntegrationTest {
    @Test
    public void integrationTestExample() {
        assertTrue("Integration test with category runs", true);
    }
}
