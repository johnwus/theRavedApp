package com.raved.analytics.repository;

import com.raved.analytics.model.ContentMetrics;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class ContentMetricsRepositoryIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics_test"));
    }

    @Autowired
    private ContentMetricsRepository repo;

    @Test
    void save_and_findByContentId_and_topByEngagementScore() {
        ContentMetrics c = new ContentMetrics();
        c.setContentId("c-1");
        c.setContentType("POST");
        c.setEngagementRate(new BigDecimal("0.55"));
        c.setViewsCount(100);
        c.setLikesCount(10);
        c.setCommentsCount(5);
        c.setCreatedAt(LocalDateTime.now());
        repo.save(c);

        assertThat(repo.findByContentId("c-1")).hasSize(1);
        assertThat(repo.findTopByEngagementScore(org.springframework.data.domain.PageRequest.of(0,1)).getContent())
                .extracting(ContentMetrics::getContentId).contains("c-1");
    }
}

