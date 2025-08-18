package com.raved.content.repository;

import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Mock FollowRepository for TheRavedApp Content Service
 * 
 * Note: Follow functionality is handled by the social service.
 * This is a temporary mock implementation for content service compilation.
 */
@Repository
public class FollowRepository {

    public List<String> findFollowingIdsByFollowerId(String followerId) {
        // Mock implementation - follow functionality is in social service
        return List.of();
    }

    public List<String> findFollowerIdsByFollowingId(String followingId) {
        // Mock implementation - follow functionality is in social service
        return List.of();
    }

    public long countFollowingByFollowerId(String followerId) {
        // Mock implementation - follow functionality is in social service
        return 0L;
    }

    public long countFollowersByFollowingId(String followingId) {
        // Mock implementation - follow functionality is in social service
        return 0L;
    }
}