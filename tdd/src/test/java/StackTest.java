import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackTest {

    private MyStack subject;

    @BeforeEach
    void setUp() {
        subject = new MyStack();
    }

    @Test
    void empty() {
        // Given

        // When
        final boolean result = subject.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void notEmptyAfterPush() {
        subject.push(1);
        final boolean result = subject.isEmpty();

        assertFalse(result);
    }

    @Test
    void sizeZeroWhenEmpty() {

        int size = subject.size();

        assertEquals(0, size);
    }

    @Test
    void sizeOneAfterPush() {

        subject.push(1);
        int size = subject.size();

        assertEquals(1, size);
    }


}
