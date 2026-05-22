# rabbitmq-consumer

Connects to a RabbitMQ queue and either writes each received message payload to a JSON file on disk (**consume** mode) or moves messages from one queue to another (**move** mode).

## Requirements

- Java 17+
- Maven 3.6+
- A running RabbitMQ broker

## Build

```bash
mvn package
```

This produces a fat jar at `target/rabbitmq-consumer-1.0-SNAPSHOT.jar`.

## Usage

### Consume mode (default)

Listens on a queue and writes each message to a local JSON file.

```bash
java -jar target/rabbitmq-consumer-1.0-SNAPSHOT.jar -q <queue> --uri <amqp-uri>
```

### Move mode

Moves messages from a source queue to a destination queue, preserving message properties.

```bash
java -jar target/rabbitmq-consumer-1.0-SNAPSHOT.jar --mode move -q <source-queue> --dest-queue <dest-queue> --uri <amqp-uri> [--dest-uri <amqp-uri>] [--count <n>]
```

### Flags

| Flag           | Description                                                  | Required             |
|----------------|--------------------------------------------------------------|----------------------|
| `-q`           | Source queue name                                            | yes                  |
| `--uri`        | AMQPS URI (`amqps://user:password@host:port/vhost`)          | yes                  |
| `--mode`       | `consume` (default) or `move`                                | no                   |
| `--dest-queue` | Destination queue name                                       | yes (move mode)      |
| `--dest-uri`   | AMQP URI for the destination broker (defaults to `--uri`)    | no                   |
| `--count`      | Max number of messages to move; omit to move all available   | no                   |

### Examples

```bash
# Consume – localhost with TLS
java -jar target/rabbitmq-consumer-1.0-SNAPSHOT.jar -q orders --uri amqps://guest:guest@localhost

# Move all messages from dead-letter queue back to the main queue
java -jar target/rabbitmq-consumer-1.0-SNAPSHOT.jar \
  --mode move \
  -q orders.dlq \
  --dest-queue orders \
  --uri amqps://guest:guest@localhost

# Move 10 messages between different brokers
java -jar target/rabbitmq-consumer-1.0-SNAPSHOT.jar \
  --mode move \
  -q events \
  --dest-queue events \
  --uri amqps://user:pass@broker-a.example.com:5671/prod \
  --dest-uri amqps://user:pass@broker-b.example.com:5671/prod \
  --count 10
```

## Output (consume mode)

Each message is written to `<queue>/message-<epoch-millis>.json` (relative to the current working directory). The folder is created automatically if it does not exist.

If the payload is valid JSON it is pretty-printed; otherwise it is wrapped in a JSON string node:

```json
"plain text payload"
```

## Notes

- Messages are **manually acknowledged** after being written (consume) or successfully published to the destination (move), so no message is lost on crash.
- In move mode the destination queue is declared as **durable** automatically.
- Move mode uses `basicGet` (polling) to drain messages; it exits when the source queue is empty or `--count` is reached.
