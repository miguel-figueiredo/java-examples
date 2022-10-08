package org.example.modules.hello.impl;

import org.example.modules.hello.HelloInterface;

public class HelloModules implements HelloInterface {

    @Override
    public void sayHello() {
        System.out.println("Hello!");
    }
}