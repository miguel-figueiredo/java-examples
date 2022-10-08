module org.example.modules.hello {
    exports org.example.modules.hello;
    provides org.example.modules.hello.HelloInterface with org.example.modules.hello.HelloModules;
}