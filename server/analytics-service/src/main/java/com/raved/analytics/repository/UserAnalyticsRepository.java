package com.raved.analytics.repository;

import com.raved.analytics.model.UserAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserAnalyticsRepository extends MongoRepository<UserAnalytics, String> {
    List<UserAnalytics> findByUserIdAndPeriodAndDateBetween(String userId, String period, LocalDate start, LocalDate end);
}

