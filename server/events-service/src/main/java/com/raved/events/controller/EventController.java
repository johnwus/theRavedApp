package com.raved.events.controller;

import com.raved.events.dto.request.CreateEventRequest;
import com.raved.events.dto.response.EventResponse;
import com.raved.events.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for managing events
 */
@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin(origins = "*")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    /**
     * Create a new event
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        logger.info("Creating new event: {}", request.getTitle());

        try {
            EventResponse response = eventService.createEvent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get event by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        logger.debug("Getting event by ID: {}", id);

        try {
            EventResponse response = eventService.getEventById(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing event
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody CreateEventRequest request) {
        logger.info("Updating event with ID: {}", id);

        try {
            EventResponse response = eventService.updateEvent(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Event not found for update: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete an event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Deleting event with ID: {}", id);

        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get events by organizer
     */
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<Page<EventResponse>> getEventsByOrganizer(
            @PathVariable Long organizerId,
            Pageable pageable) {
        logger.debug("Getting events for organizer ID: {}", organizerId);

        try {
            Page<EventResponse> response = eventService.getEventsByOrganizer(organizerId, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting events by organizer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get events by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<EventResponse>> getEventsByCategory(
            @PathVariable String category,
            Pageable pageable) {
        logger.debug("Getting events for category: {}", category);

        try {
            Page<EventResponse> response = eventService.getEventsByCategory(category, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting events by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get upcoming events
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventResponse>> getUpcomingEvents(Pageable pageable) {
        logger.debug("Getting upcoming events");

        try {
            Page<EventResponse> response = eventService.getUpcomingEvents(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting upcoming events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get featured events
     */
    @GetMapping("/featured")
    public ResponseEntity<List<EventResponse>> getFeaturedEvents() {
        logger.debug("Getting featured events");

        try {
            List<EventResponse> response = eventService.getFeaturedEvents();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting featured events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search events
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EventResponse>> searchEvents(
            @RequestParam String query,
            Pageable pageable) {
        logger.debug("Searching events with query: {}", query);

        try {
            Page<EventResponse> response = eventService.searchEvents(query, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get events by location
     */
    @GetMapping("/location")
    public ResponseEntity<Page<EventResponse>> getEventsByLocation(
            @RequestParam String location,
            Pageable pageable) {
        logger.debug("Getting events for location: {}", location);

        try {
            Page<EventResponse> response = eventService.getEventsByLocation(location, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting events by location: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get events by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<EventResponse>> getEventsByDateRange(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate,
            Pageable pageable) {
        logger.debug("Getting events between {} and {}", startDate, endDate);

        try {
            Page<EventResponse> response = eventService.getEventsByDateRange(startDate, endDate, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting events by date range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Toggle event feature status
     */
    @PatchMapping("/{id}/feature")
    public ResponseEntity<EventResponse> toggleEventFeature(@PathVariable Long id) {
        logger.info("Toggling feature status for event ID: {}", id);

        try {
            EventResponse response = eventService.toggleEventFeature(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Event not found for feature toggle: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error toggling event feature: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get event statistics
     */
    @GetMapping("/stats/organizer/{organizerId}")
    public ResponseEntity<Long> getEventCountByOrganizer(@PathVariable Long organizerId) {
        logger.debug("Getting event count for organizer ID: {}", organizerId);

        try {
            long count = eventService.getEventCountByOrganizer(organizerId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting event count by organizer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/category/{category}")
    public ResponseEntity<Long> getEventCountByCategory(@PathVariable String category) {
        logger.debug("Getting event count for category: {}", category);

        try {
            long count = eventService.getEventCountByCategory(category);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting event count by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
