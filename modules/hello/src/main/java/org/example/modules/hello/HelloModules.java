package org.example.modules.hello;

public class HelloModules implements HelloInterface {

    public static void doSomething() {
        System.out.println("Hello, Modules!");
    }

    @Override
    public void sayHello() {
        System.out.println("Hello!");
    }
}