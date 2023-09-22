package org.test;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {

        final URI endpointOverride = URI.create("http://localhost:4566");
        final Region region = Region.US_EAST_1;
        final Boolean forcePathStyle = false;
        final String accessKey = "access-key";
        final String secretKey = "secret-key";
        final String bucket = "bucket";
        final String key = "/test/test100k.db";
        final URL sourceUrl = new URL("http://speedtest.ftp.otenet.gr/files/test100k.db");

        final S3Client client = S3Client.builder()
                .endpointOverride(endpointOverride)
                .region(region)
                .forcePathStyle(forcePathStyle)
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .build();

        var uc = sourceUrl.openConnection();

        // The buffered input stream is used to increment the default buffer size
        // in order to prevent ResetExceptions.
        // See: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/best-practices.html
        try (InputStream is = new BufferedInputStream(uc.getInputStream(), 262144)){
        client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(is, uc.getContentLengthLong()));
        }

    }
}
