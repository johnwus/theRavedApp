# Kafka Topics Provisioning (Examples)

Define topics per service to align with code expectations. Apply via a Kafka operator or CLI scripts.

## Suggested topics
- user-service
  - user.created (partitions: 3, replication: 1)
  - user.updated (3,1)
  - user.verified (3,1)
- notification-service
  - notification-events (3,1)
  - user-events (3,1)
- content-service
  - content.created (3,1)
  - content.flagged (3,1)
- analytics-service
  - analytics.ingest (3,1)

## kubectl + Strimzi (example)
```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaTopic
metadata:
  name: user.created
  labels:
    strimzi.io/cluster: raved-kafka
spec:
  partitions: 3
  replicas: 1
```

## CLI example
```bash
kafka-topics --bootstrap-server kafka:9092 --create --topic user.created --partitions 3 --replication-factor 1
```

