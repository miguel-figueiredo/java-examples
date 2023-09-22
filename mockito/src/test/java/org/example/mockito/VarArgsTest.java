package org.example.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VarArgsTest {

    @Mock
    MockedClass mock;

    @Captor
    ArgumentCaptor<String[]> captor;

    @Test
    void testVargs() {
        mock.varargs("a", "b", "c");

        verify(mock).varargs(captor.capture());
    }

    static class MockedClass {

        public void varargs(String... args) {
        }
    }
}
