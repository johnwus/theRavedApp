package com.raved.analytics.repository;

import com.raved.analytics.model.RankingSnapshot;
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
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class RankingSnapshotRepositoryIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics_test"));
    }

    @Autowired
    private RankingSnapshotRepository repo;

    @Test
    void save_and_findByCompositeKeys() {
        RankingSnapshot snap = new RankingSnapshot();
        snap.setSnapshotType("USER");
        snap.setMetric("ENGAGEMENT");
        snap.setPeriod("WEEKLY");
        snap.setDate(LocalDate.of(2025, 8, 11));
        snap.setCategory("tech");
        RankingSnapshot.Item item = new RankingSnapshot.Item();
        item.setEntityId("u1");
        item.setRank(1);
        item.setScore(BigDecimal.valueOf(99.5));
        snap.setItems(List.of(item));

        repo.save(snap);

        assertThat(repo.findBySnapshotTypeAndMetricAndPeriodAndDate("USER","ENGAGEMENT","WEEKLY", LocalDate.of(2025,8,11)))
                .hasSize(1);
    }
}

