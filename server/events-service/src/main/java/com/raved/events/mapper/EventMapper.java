package com.raved.events.mapper;

import com.raved.events.dto.request.CreateEventRequest;
import com.raved.events.dto.response.EventResponse;
import com.raved.events.model.Event;
import org.springframework.stereotype.Component;

/**
 * Mapper for Event entity and DTOs
 */
@Component
public class EventMapper {

    public EventResponse toEventResponse(Event event) {
        if (event == null) {
            return null;
        }

        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setOrganizerId(event.getOrganizerId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setImageUrl(event.getImageUrl());
        response.setOrgAvatarUrl(event.getOrgAvatarUrl());
        response.setLocationName(event.getLocationName());
        response.setLocationGeo(event.getLocationGeo());
        response.setStartsAt(event.getStartsAt());
        response.setEndsAt(event.getEndsAt());
        response.setCategory(event.getCategory());
        response.setAudience(event.getAudience());
        response.setPriceAmount(event.getPriceAmount());
        response.setCurrency(event.getCurrency());
        response.setCapacity(event.getCapacity());
        response.setIsFeatured(event.getIsFeatured());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // TODO: Set computed fields when attendee service is available
        response.setAttendeeCount(0);
        response.setIsUserAttending(false);
        response.setIsUserInterested(false);

        return response;
    }

    public Event toEvent(CreateEventRequest request) {
        if (request == null) {
            return null;
        }

        Event event = new Event();
        event.setOrganizerId(request.getOrganizerId());
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

        return event;
    }
}
