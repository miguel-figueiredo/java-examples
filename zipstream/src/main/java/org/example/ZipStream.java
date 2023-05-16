package org.example;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipStream {

    public static void main(String[] args) throws IOException {

        var urls = List.of(
                new URL("https://speed.hetzner.de/100MB.bin"),
                new URL("http://speedtest.ftp.otenet.gr/files/test10Mb.db"));

        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream("/tmp/output.zip"))){
            for(URL url : urls) {
                try (InputStream in = url.openStream()) {
                    ZipEntry ze = new ZipEntry(url.getFile());
                    out.putNextEntry(ze);
                    IOUtils.copy(in, out);
                    out.closeEntry();
                }
            }
        }
    }
}
