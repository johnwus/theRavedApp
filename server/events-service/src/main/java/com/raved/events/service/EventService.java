package com.raved.events.service;

import com.raved.events.dto.request.CreateEventRequest;
import com.raved.events.dto.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * Service interface for managing events
 */
public interface EventService {

    /**
     * Create a new event
     */
    EventResponse createEvent(CreateEventRequest request);

    /**
     * Get event by ID
     */
    EventResponse getEventById(Long id);

    /**
     * Update an existing event
     */
    EventResponse updateEvent(Long id, CreateEventRequest request);

    /**
     * Delete an event
     */
    void deleteEvent(Long id);

    /**
     * Get events by organizer
     */
    Page<EventResponse> getEventsByOrganizer(Long organizerId, Pageable pageable);

    /**
     * Get events by category
     */
    Page<EventResponse> getEventsByCategory(String category, Pageable pageable);

    /**
     * Get events by audience
     */
    Page<EventResponse> getEventsByAudience(String audience, Pageable pageable);

    /**
     * Get upcoming events
     */
    Page<EventResponse> getUpcomingEvents(Pageable pageable);

    /**
     * Get featured events
     */
    List<EventResponse> getFeaturedEvents();

    /**
     * Search events
     */
    Page<EventResponse> searchEvents(String query, Pageable pageable);

    /**
     * Get events by location
     */
    Page<EventResponse> getEventsByLocation(String location, Pageable pageable);

    /**
     * Get events by date range
     */
    Page<EventResponse> getEventsByDateRange(Instant startDate, Instant endDate, Pageable pageable);

    /**
     * Feature/unfeature an event
     */
    EventResponse toggleEventFeature(Long eventId);

    /**
     * Get event statistics
     */
    long getEventCountByOrganizer(Long organizerId);

    long getEventCountByCategory(String category);
}
