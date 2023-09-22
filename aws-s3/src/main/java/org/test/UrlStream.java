package org.test;

import java.io.IOException;
import java.net.URL;

public class UrlStream {

    public static void main(String[] args) throws IOException {
        var url = new URL("https://console.emea.ocp.int.kn/static/assets/openshift-platform-logo.svg");
        var connection = url.openConnection();

        System.out.println(connection.getHeaderFields());
        System.out.println(connection.getContentLengthLong());
        System.out.println(connection.getHeaderField("Content-Length"));
    }
}
