package org.example.modules.main;

import org.example.modules.hello.HelloInterface;
import org.example.modules.hello.HelloModules;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        HelloModules.doSomething();

        Iterable<HelloInterface> services = ServiceLoader.load(HelloInterface.class);
        HelloInterface service = services.iterator().next();
        service.sayHello();
    }
}
