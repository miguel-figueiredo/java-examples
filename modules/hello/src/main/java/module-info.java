import org.example.modules.hello.HelloInterface;
import org.example.modules.hello.impl.HelloModules;

module org.example.modules.hello {
    exports org.example.modules.hello;
    provides HelloInterface with HelloModules;
}