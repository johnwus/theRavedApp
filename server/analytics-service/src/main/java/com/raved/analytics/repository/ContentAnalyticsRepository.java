package com.raved.analytics.repository;

import com.raved.analytics.model.ContentAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContentAnalyticsRepository extends MongoRepository<ContentAnalytics, String> {
    List<ContentAnalytics> findByContentIdAndPeriodAndDateBetween(String contentId, String period, LocalDate start, LocalDate end);
}

