package com.raved.analytics.repository;

import com.raved.analytics.model.RankingSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RankingSnapshotRepository extends MongoRepository<RankingSnapshot, String> {
    List<RankingSnapshot> findBySnapshotTypeAndMetricAndPeriodAndDate(String snapshotType, String metric, String period, LocalDate date);
}

