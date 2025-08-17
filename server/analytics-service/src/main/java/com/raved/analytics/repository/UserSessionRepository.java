package com.raved.analytics.repository;

import com.raved.analytics.model.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSession, String> {
    List<UserSession> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
