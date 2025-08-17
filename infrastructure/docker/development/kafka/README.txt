Kafka Dev Notes 
 
- Compose uses Confluent images for Zookeeper/Kafka. 
- Exposes localhost:9092 for local producers/consumers. 
- Adjust KAFKA_ADVERTISED_LISTENERS in compose if accessing from other hosts.
