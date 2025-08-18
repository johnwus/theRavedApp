package com.raved.user.it;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import static org.assertj.core.api.Assertions.assertThat;
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

import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.service.AuthService;
import com.raved.user.service.ProfileService;

@Testcontainers
@SpringBootTest
class ProfileServiceUpdateIT {

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
        registry.add("kafka.topics.user-updated", () -> "user.updated");
    }

    static KafkaConsumer<String, String> consumer;

    @BeforeAll
    static void setupConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "profile-it");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("user.updated"));
    }

    @AfterAll
    static void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileService profileService;

    @Test
    void updateProfile_persistsAndEmitsEvent() {
        // First register a user to get a valid ID
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("profuser");
        reg.setEmail("prof@raved.app");
        reg.setPassword("P4ssword!");
        reg.setConfirmPassword("P4ssword!");
        reg.setFirstName("Prof");
        reg.setLastName("User");
        var authResp = authService.register(reg);
        Long userId = authResp.getUser().getId();

        UpdateProfileRequest upd = new UpdateProfileRequest();
        upd.setFirstName("Professor");
        upd.setLastName("User");
        upd.setBio("Updated bio");
        upd.setPhoneNumber("+1-202-555-0100");
        upd.setProfilePictureUrl("https://cdn.raved.app/p/it.png");
        UserResponse updated = profileService.updateProfile(userId, upd);
        assertThat(updated.getFirstName()).isEqualTo("Professor");
        assertThat(updated.getLastName()).isEqualTo("User");
        assertThat(updated.getBio()).isEqualTo("Updated bio");
        assertThat(updated.getPhoneNumber()).isEqualTo("+1-202-555-0100");
        assertThat(updated.getProfilePictureUrl()).isEqualTo("https://cdn.raved.app/p/it.png");

        ConsumerRecord<String, String> rec = consumer.poll(Duration.ofSeconds(10)).iterator().next();
        assertThat(rec.key()).isEqualTo(String.valueOf(userId));
        assertThat(rec.value()).contains("\"userProfile\"");
        assertThat(rec.value()).contains("\"firstName\":\"Professor\"");
        assertThat(rec.value()).contains("\"lastName\":\"User\"");
        assertThat(rec.value()).contains("\"bio\":\"Updated bio\"");
        assertThat(rec.value()).contains("\"phoneNumber\":\"+1-202-555-0100\"");
        assertThat(rec.value()).contains("\"profilePictureUrl\":\"https://cdn.raved.app/p/it.png\"");
    }
}
