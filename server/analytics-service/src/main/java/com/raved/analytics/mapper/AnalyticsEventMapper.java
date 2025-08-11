package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

/**
 * Mapper for AnalyticsEvent entities and DTOs
 */
@Component
public class AnalyticsEventMapper {

    /**
     * Convert TrackEventRequest to AnalyticsEvent entity
     */
    public AnalyticsEvent toAnalyticsEvent(TrackEventRequest request) {
        if (request == null) {
            return null;
        }

        AnalyticsEvent event = new AnalyticsEvent();
        event.setUserId(request.getUserId());
        
        // Convert string event type to enum
        try {
            event.setEventType(AnalyticsEvent.EventType.valueOf(request.getEventType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            event.setEventType(AnalyticsEvent.EventType.PAGE_VIEW); // Default to PAGE_VIEW instead of CUSTOM
        }
        
        event.setEntityId(request.getEntityId());
        event.setEntityType(request.getEntityType());
        event.setSessionId(request.getSessionId());
        event.setUserAgent(request.getUserAgent());
        event.setIpAddress(request.getIpAddress());
        event.setPlatform(request.getPlatform());
        
        // Convert properties to JSON string if needed
        if (request.getProperties() != null) {
            // TODO: Convert Map to JSON string
            // For now, store as string representation
            event.setEventData(request.getProperties().toString());
        }

        return event;
    }

    /**
     * Convert AnalyticsEvent entity to AnalyticsEventResponse DTO
     */
    public AnalyticsEventResponse toAnalyticsEventResponse(AnalyticsEvent event) {
        if (event == null) {
            return null;
        }

        AnalyticsEventResponse response = new AnalyticsEventResponse();
        response.setId(event.getId());
        response.setUserId(event.getUserId());
        response.setEventType(convertEventType(event.getEventType()));
        response.setTargetId(event.getEntityId());
        response.setTargetType(event.getEntityType());
        response.setSessionId(event.getSessionId());
        response.setUserAgent(event.getUserAgent());
        response.setIpAddress(event.getIpAddress());
        response.setPlatform(event.getPlatform());
        response.setTimestamp(event.getEventTimestamp());
        response.setCreatedAt(event.getCreatedAt());
        
        // TODO: Parse JSON string back to Map
        // For now, leave properties null
        
        return response;
    }

    /**
     * Convert EventType enum to response EventType
     */
    private com.raved.analytics.model.EventType convertEventType(AnalyticsEvent.EventType eventType) {
        if (eventType == null) {
            return null;
        }
        try {
            return com.raved.analytics.model.EventType.valueOf(eventType.name());
        } catch (IllegalArgumentException e) {
            return com.raved.analytics.model.EventType.PAGE_VIEW; // Default fallback
        }
    }
}
