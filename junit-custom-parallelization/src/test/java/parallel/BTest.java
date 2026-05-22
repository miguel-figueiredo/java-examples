package parallel;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("B")
class BTest {
    @Test
    void name() throws InterruptedException {
        Thread.sleep(5000);
    }
}