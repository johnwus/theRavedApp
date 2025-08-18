package com.raved.events.service.impl;

import com.raved.events.dto.request.CreateEventRequest;
import com.raved.events.dto.response.EventResponse;
import com.raved.events.mapper.EventMapper;
import com.raved.events.model.Event;
import com.raved.events.repository.EventRepository;
import com.raved.events.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of EventService
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        logger.info("Creating new event: {}", request.getTitle());

        Event event = eventMapper.toEvent(request);
        Event savedEvent = eventRepository.save(event);

        logger.info("Event created successfully with ID: {}", savedEvent.getId());
        return eventMapper.toEventResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        logger.debug("Getting event by ID: {}", id);

        Optional<Event> eventOpt = eventRepository.findById(id);
        return eventOpt.map(eventMapper::toEventResponse).orElse(null);
    }

    @Override
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        logger.info("Updating event with ID: {}", id);

        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Event not found with ID: " + id);
        }

        Event event = eventOpt.get();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setImageUrl(request.getImageUrl());
        event.setOrgAvatarUrl(request.getOrgAvatarUrl());
        event.setLocationName(request.getLocationName());
        event.setLocationGeo(request.getLocationGeo());
        event.setStartsAt(request.getStartsAt());
        event.setEndsAt(request.getEndsAt());
        event.setCategory(request.getCategory());
        event.setAudience(request.getAudience());
        event.setPriceAmount(request.getPriceAmount());
        event.setCurrency(request.getCurrency());
        event.setCapacity(request.getCapacity());
        event.setIsFeatured(request.getIsFeatured());
        event.setUpdatedAt(Instant.now());

        Event savedEvent = eventRepository.save(event);
        logger.info("Event updated successfully with ID: {}", id);

        return eventMapper.toEventResponse(savedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        logger.info("Deleting event with ID: {}", id);

        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            logger.info("Event deleted successfully with ID: {}", id);
        } else {
            logger.warn("Event not found for deletion with ID: {}", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByOrganizer(Long organizerId, Pageable pageable) {
        logger.debug("Getting events for organizer ID: {}", organizerId);

        Page<Event> events = eventRepository.findByOrganizerIdOrderByStartsAtDesc(organizerId, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByCategory(String category, Pageable pageable) {
        logger.debug("Getting events for category: {}", category);

        Page<Event> events = eventRepository.findByCategoryOrderByStartsAtDesc(category, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByAudience(String audience, Pageable pageable) {
        logger.debug("Getting events for audience: {}", audience);

        Page<Event> events = eventRepository.findByAudienceOrderByStartsAtDesc(audience, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getUpcomingEvents(Pageable pageable) {
        logger.debug("Getting upcoming events");

        Page<Event> events = eventRepository.findUpcomingEvents(Instant.now(), pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getFeaturedEvents() {
        logger.debug("Getting featured events");

        List<Event> events = eventRepository.findByIsFeaturedTrueOrderByStartsAtDesc();
        return events.stream()
                .map(eventMapper::toEventResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> searchEvents(String query, Pageable pageable) {
        logger.debug("Searching events with query: {}", query);

        Page<Event> events = eventRepository.searchEvents(query, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByLocation(String location, Pageable pageable) {
        logger.debug("Getting events for location: {}", location);

        Page<Event> events = eventRepository.findByLocation(location, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByDateRange(Instant startDate, Instant endDate, Pageable pageable) {
        logger.debug("Getting events between {} and {}", startDate, endDate);

        Page<Event> events = eventRepository.findByDateRange(startDate, endDate, pageable);
        return events.map(eventMapper::toEventResponse);
    }

    @Override
    public EventResponse toggleEventFeature(Long eventId) {
        logger.info("Toggling feature status for event ID: {}", eventId);

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }

        Event event = eventOpt.get();
        event.setIsFeatured(!event.getIsFeatured());
        event.setUpdatedAt(Instant.now());

        Event savedEvent = eventRepository.save(event);
        logger.info("Event feature status toggled to {} for ID: {}", event.getIsFeatured(), eventId);

        return eventMapper.toEventResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountByOrganizer(Long organizerId) {
        return eventRepository.countByOrganizerId(organizerId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountByCategory(String category) {
        return eventRepository.countByCategory(category);
    }
}
