package com.raved.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FollowRepository for TheRavedApp
 */
@Repository
public interface FollowRepository extends JpaRepository<Object, Long> {
    
    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
    
    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :followingId")
    List<Long> findFollowerIdsByFollowingId(@Param("followingId") Long followingId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :followerId")
    long countFollowingByFollowerId(@Param("followerId") Long followerId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingId = :followingId")
    long countFollowersByFollowingId(@Param("followingId") Long followingId);
} 