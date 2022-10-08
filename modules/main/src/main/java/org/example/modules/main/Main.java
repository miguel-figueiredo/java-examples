package org.example.modules.main;

import org.example.modules.hello.HelloInterface;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader.load(HelloInterface.class)
                .findFirst()
                .ifPresent(HelloInterface::sayHello);
    }
}
