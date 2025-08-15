package com.raved.user.it;

import com.raved.user.event.UserEventPublisher;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class UsersEventsIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("user_service_test")
            .withUsername("raved_admin")
            .withPassword("theRAVEDapp#123");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3"));

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("kafka.topics.user-events", () -> "user-events");
        registry.add("kafka.topics.user-created", () -> "user.created");
        registry.add("kafka.topics.user-updated", () -> "user.updated");
    }

    @Autowired
    private UserEventPublisher publisher;

    static KafkaConsumer<String, String> consumer;

    @BeforeAll
    static void setupConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "users-it");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(java.util.Arrays.asList("user.created", "user.updated"));
    }

    @AfterAll
    static void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void publishesUserCreatedEvent() {
        publisher.publishUserCreated(101L, "it-user", "it@raved.app");
        ConsumerRecord<String, String> rec = consumer.poll(Duration.ofSeconds(10)).iterator().next();
        assertThat(rec).isNotNull();
        assertThat(rec.key()).isEqualTo("101");
        assertThat(rec.value()).contains("\"username\":\"it-user\"");
    }

    @Test
    void publishesUserUpdatedEvent() {
        Map<String, Object> profile = new java.util.HashMap<>();
        profile.put("displayName", "IT User");
        profile.put("email", "it@raved.app");
        publisher.publishUserUpdated(202L, profile);
        var records = consumer.poll(Duration.ofSeconds(10));
        boolean found = false;
        for (ConsumerRecord<String, String> r : records) {
            if ("user.updated".equals(r.topic()) && "202".equals(r.key())) {
                assertThat(r.value()).contains("\"userProfile\"");
                assertThat(r.value()).contains("\"displayName\":\"IT User\"");
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }
}
