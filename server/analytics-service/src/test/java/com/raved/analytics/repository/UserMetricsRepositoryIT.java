package com.raved.analytics.repository;

import com.raved.analytics.model.UserMetrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class UserMetricsRepositoryIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics_test"));
    }

    @Autowired
    private UserMetricsRepository repo;

    @Test
    void save_and_findByUserId_and_topByEngagementRate() {
        UserMetrics u = new UserMetrics();
        u.setUserId("u-top");
        u.setEngagementScore(new BigDecimal("0.75"));
        u.setInfluenceScore(new BigDecimal("0.40"));
        u.setLastActiveDate(LocalDateTime.now());
        repo.save(u);

        assertThat(repo.findByUserId("u-top")).isPresent();
        assertThat(repo.findTopByEngagementRate(org.springframework.data.domain.PageRequest.of(0,1)).getContent())
                .extracting(UserMetrics::getUserId).contains("u-top");
    }
}

