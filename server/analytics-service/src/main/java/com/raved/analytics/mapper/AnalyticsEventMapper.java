package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
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
            event.setEventType(EventType.valueOf(request.getEventType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            event.setEventType(EventType.CUSTOM);
        }
        
        event.setEventName(request.getEventName());
        event.setTargetId(request.getEntityId());
        event.setTargetType(request.getEntityType());
        event.setSessionId(request.getSessionId());
        event.setUserAgent(request.getUserAgent());
        event.setIpAddress(request.getIpAddress());
        event.setReferrer(request.getReferrer());
        event.setPlatform(request.getPlatform());
        
        // Convert properties to JSON string if needed
        if (request.getProperties() != null) {
            // TODO: Convert Map to JSON string
            // For now, store as string representation
            event.setProperties(request.getProperties().toString());
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
        response.setEventType(event.getEventType());
        response.setEventName(event.getEventName());
        response.setTargetId(event.getTargetId());
        response.setTargetType(event.getTargetType());
        response.setSessionId(event.getSessionId());
        response.setUserAgent(event.getUserAgent());
        response.setIpAddress(event.getIpAddress());
        response.setReferrer(event.getReferrer());
        response.setPlatform(event.getPlatform());
        response.setTimestamp(event.getTimestamp());
        response.setCreatedAt(event.getCreatedAt());
        
        // TODO: Parse JSON string back to Map
        // For now, leave properties null
        
        return response;
    }
}
