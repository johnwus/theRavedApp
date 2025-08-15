package com.raved.user.it;

import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.repository.UserRepository;
import com.raved.user.service.AuthService;
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
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class AuthServiceRegistrationIT {

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
        registry.add("kafka.topics.user-created", () -> "user.created");
    }

    static KafkaConsumer<String, String> consumer;

    @BeforeAll
    static void setupConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "auth-it");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(java.util.Collections.singletonList("user.created"));
    }

    @AfterAll
    static void tearDown() {
        if (consumer != null) consumer.close();
    }

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;

    @Test
    void register_persistsUser_andPublishesEvent() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("reguser");
        req.setEmail("reg@raved.app");
        req.setPassword("P4ssword!");
        req.setConfirmPassword("P4ssword!");
        req.setFirstName("Reg");
        req.setLastName("User");

        AuthResponse resp = authService.register(req);
        assertThat(resp).isNotNull();
        assertThat(resp.getUser()).isNotNull();
        Long userId = resp.getUser().getId();
        assertThat(userRepository.findById(userId)).isPresent();

        ConsumerRecord<String, String> rec = consumer.poll(Duration.ofSeconds(10)).iterator().next();
        assertThat(rec.key()).isEqualTo(String.valueOf(userId));
        assertThat(rec.value()).contains("\"username\":\"reguser\"");
    }
}

