package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Consumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String USAGE = """
            Usage:
              Consume mode (default):
                -q <queue> --uri <amqp-uri> [--count <n>]
            
              Move mode:
                --mode move -q <source-queue> --dest-queue <destination-queue> --uri <amqp-uri> [--dest-uri <amqp-uri>] [--count <n>]
            
            Flags:
              -q            source queue name (required)
              --uri         AMQP URI for the source broker (required)
              --mode        'consume' (default) or 'move'
              --dest-queue  destination queue name (required for move mode)
              --dest-uri    AMQP URI for the destination broker (defaults to --uri)
              --count       max number of messages to consume or move; omit to process all
            """;

    public static void main(String[] args) throws Exception {
        Map<String, String> flags = parseFlags(args);

        String mode  = flags.getOrDefault("--mode", "consume");
        String queue = require(flags, "-q");
        String uri   = require(flags, "--uri");

        switch (mode) {
            case "consume" -> runConsume(queue, uri, flags);
            case "move"    -> runMove(queue, uri, flags);
            default -> {
                System.err.println("Unknown mode: " + mode);
                System.err.println(USAGE);
                System.exit(1);
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Consume mode – writes each message to a local JSON file
    // ──────────────────────────────────────────────────────────────────────

    private static void runConsume(String queue, String uri, Map<String, String> flags) throws Exception {
        String countStr = flags.get("--count");
        int maxCount = (countStr != null) ? Integer.parseInt(countStr) : Integer.MAX_VALUE;

        Path outputDir = Path.of(queue);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        configureSsl(factory, uri);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        if (maxCount == Integer.MAX_VALUE) {
            System.out.printf("Waiting for messages on queue '%s'. Press Ctrl+C to exit.%n", queue);
        } else {
            System.out.printf("Consuming up to %d message(s) from queue '%s'.%n", maxCount, queue);
        }

        final int[] count = {0};
        final int limit = maxCount;

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            JsonNode node = parsePayload(body);
            Path file = writeToFile(node, outputDir);
            System.out.printf("Written message to %s%n", file);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            count[0]++;
            if (count[0] >= limit) {
                System.out.printf("Reached message limit (%d). Exiting.%n", limit);
                try {
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    // ignore on shutdown
                }
                System.exit(0);
            }
        };

        channel.basicConsume(queue, false, deliverCallback, consumerTag -> {});
    }

    // ──────────────────────────────────────────────────────────────────────
    // Move mode – moves messages from source queue to destination queue
    // ──────────────────────────────────────────────────────────────────────

    private static void runMove(String sourceQueue, String sourceUri, Map<String, String> flags) throws Exception {
        String destQueue = require(flags, "--dest-queue");
        String destUri   = flags.getOrDefault("--dest-uri", sourceUri);
        String countStr  = flags.get("--count");
        int maxCount     = (countStr != null) ? Integer.parseInt(countStr) : Integer.MAX_VALUE;

        // Source connection
        ConnectionFactory srcFactory = new ConnectionFactory();
        srcFactory.setUri(sourceUri);
        configureSsl(srcFactory, sourceUri);
        Connection srcConnection = srcFactory.newConnection();
        Channel srcChannel = srcConnection.createChannel();

        // Destination connection (may be same broker)
        ConnectionFactory destFactory = new ConnectionFactory();
        destFactory.setUri(destUri);
        configureSsl(destFactory, destUri);
        Connection destConnection = destFactory.newConnection();
        Channel destChannel = destConnection.createChannel();

        System.out.printf("Moving messages from '%s' to '%s'...%n", sourceQueue, destQueue);

        int moved = 0;
        while (moved < maxCount) {
            GetResponse response = srcChannel.basicGet(sourceQueue, false);
            if (response == null) {
                // queue is empty
                break;
            }

            byte[] body = response.getBody();
            AMQP.BasicProperties props = response.getProps();

            // Publish to destination preserving original properties
            destChannel.basicPublish("", destQueue, props, body);

            // Acknowledge on source so the message is removed
            srcChannel.basicAck(response.getEnvelope().getDeliveryTag(), false);
            moved++;
        }

        System.out.printf("Done. Moved %d message(s) from '%s' to '%s'.%n", moved, sourceQueue, destQueue);

        destChannel.close();
        destConnection.close();
        srcChannel.close();
        srcConnection.close();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────

    private static void configureSsl(ConnectionFactory factory, String uri)
            throws NoSuchAlgorithmException, KeyManagementException {
        if (uri.startsWith("amqps://")) {
            factory.useSslProtocol();
        }
    }

    private static JsonNode parsePayload(byte[] body) {
        try {
            return MAPPER.readTree(body);
        } catch (IOException e) {
            return new TextNode(new String(body));
        }
    }

    private static Path writeToFile(JsonNode node, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Path output = outputDir.resolve("message-" + Instant.now().toEpochMilli() + ".json");
        Files.writeString(output, MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        return output;
    }

    private static Map<String, String> parseFlags(String[] args) {
        Map<String, String> flags = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            if (!args[i].startsWith("-")) {
                System.err.println("Expected a flag, got: " + args[i]);
                System.err.println(USAGE);
                System.exit(1);
            }
            flags.put(args[i], args[i + 1]);
        }
        return flags;
    }

    private static String require(Map<String, String> flags, String flag) {
        String value = flags.get(flag);
        if (value == null) {
            System.err.println("Missing required flag: " + flag);
            System.err.println(USAGE);
            System.exit(1);
        }
        return value;
    }
}
