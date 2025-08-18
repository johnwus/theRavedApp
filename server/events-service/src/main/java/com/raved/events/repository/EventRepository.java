package com.raved.events.repository;

import com.raved.events.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository for Event entities
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find events by organizer
     */
    Page<Event> findByOrganizerIdOrderByStartsAtDesc(Long organizerId, Pageable pageable);

    /**
     * Find events by category
     */
    Page<Event> findByCategoryOrderByStartsAtDesc(String category, Pageable pageable);

    /**
     * Find events by audience
     */
    Page<Event> findByAudienceOrderByStartsAtDesc(String audience, Pageable pageable);

    /**
     * Find upcoming events
     */
    @Query("SELECT e FROM Event e WHERE e.startsAt > :now ORDER BY e.startsAt ASC")
    Page<Event> findUpcomingEvents(@Param("now") Instant now, Pageable pageable);

    /**
     * Find featured events
     */
    List<Event> findByIsFeaturedTrueOrderByStartsAtDesc();

    /**
     * Search events by title or description
     */
    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY e.startsAt DESC")
    Page<Event> searchEvents(@Param("query") String query, Pageable pageable);

    /**
     * Find events by location (within radius)
     */
    @Query("SELECT e FROM Event e WHERE " +
            "e.locationName IS NOT NULL AND " +
            "LOWER(e.locationName) LIKE LOWER(CONCAT('%', :location, '%')) " +
            "ORDER BY e.startsAt DESC")
    Page<Event> findByLocation(@Param("location") String location, Pageable pageable);

    /**
     * Find events by date range
     */
    @Query("SELECT e FROM Event e WHERE " +
            "e.startsAt BETWEEN :startDate AND :endDate " +
            "ORDER BY e.startsAt ASC")
    Page<Event> findByDateRange(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable);

    /**
     * Count events by organizer
     */
    long countByOrganizerId(Long organizerId);

    /**
     * Count events by category
     */
    long countByCategory(String category);
}
