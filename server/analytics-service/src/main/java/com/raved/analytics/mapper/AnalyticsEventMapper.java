package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for AnalyticsEvent entities and DTOs
 *
 * Provides comprehensive mapping between AnalyticsEvent models, request DTOs,
 * and response DTOs with support for ecommerce, subscription, and enhanced
 * analytics.
 */
@Mapper(componentModel = "spring", imports = {LocalDateTime.class, DateTimeFormatter.class})
public interface AnalyticsEventMapper {

    // Request DTO to Entity mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventTimestamp", source = "eventTimestamp", defaultExpression = "java(LocalDateTime.now())")
    @Mapping(target = "eventDate", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().toLocalDate().toString() : LocalDateTime.now().toLocalDate().toString())")
    @Mapping(target = "eventHour", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getHour() : LocalDateTime.now().getHour())")
    @Mapping(target = "eventWeekday", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getDayOfWeek().getValue() : LocalDateTime.now().getDayOfWeek().getValue())")
    @Mapping(target = "eventMonth", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getMonthValue() : LocalDateTime.now().getMonthValue())")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "mapEventTypeToEntity")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "performanceMetrics", source = "performanceMetrics")
    @Mapping(target = "userContext", source = "userContext")
    @Mapping(target = "deviceContext", source = "deviceContext")
    @Mapping(target = "ecommerceData", source = "ecommerceData")
    @Mapping(target = "subscriptionData", source = "subscriptionData")
    @Mapping(target = "socialData", source = "socialData", qualifiedByName = "mapSocialDataRequest")
    @Mapping(target = "contentData", source = "contentData", qualifiedByName = "mapContentDataRequest")
    AnalyticsEvent toEntity(TrackEventRequest request);

    // Entity to Response DTO mapping
    @Mapping(target = "eventDate", source = "eventDate")
    @Mapping(target = "eventHour", source = "eventHour")
    @Mapping(target = "eventWeekday", source = "eventWeekday")
    @Mapping(target = "eventMonth", source = "eventMonth")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "performanceMetrics", source = "performanceMetrics")
    @Mapping(target = "userContext", source = "userContext")
    @Mapping(target = "deviceContext", source = "deviceContext")
    @Mapping(target = "ecommerceData", source = "ecommerceData")
    @Mapping(target = "subscriptionData", source = "subscriptionData")
    @Mapping(target = "socialData", source = "socialData", qualifiedByName = "mapSocialData")
    @Mapping(target = "contentData", source = "contentData", qualifiedByName = "mapContentData")
    AnalyticsEventResponse toResponse(AnalyticsEvent entity);

    // List mappings
    List<AnalyticsEventResponse> toResponseList(List<AnalyticsEvent> entities);

    // Update entity from request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventTimestamp", source = "eventTimestamp", defaultExpression = "java(LocalDateTime.now())")
    @Mapping(target = "eventDate", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().toLocalDate().toString() : LocalDateTime.now().toLocalDate().toString())")
    @Mapping(target = "eventHour", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getHour() : LocalDateTime.now().getHour())")
    @Mapping(target = "eventWeekday", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getDayOfWeek().getValue() : LocalDateTime.now().getDayOfWeek().getValue())")
    @Mapping(target = "eventMonth", expression = "java(request.getEventTimestamp() != null ? request.getEventTimestamp().getMonthValue() : LocalDateTime.now().getMonthValue())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "mapEventTypeToEntity")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "performanceMetrics", source = "performanceMetrics")
    @Mapping(target = "userContext", source = "userContext")
    @Mapping(target = "deviceContext", source = "deviceContext")
    @Mapping(target = "ecommerceData", source = "ecommerceData")
    @Mapping(target = "subscriptionData", source = "subscriptionData")
    @Mapping(target = "socialData", source = "socialData", qualifiedByName = "mapSocialDataRequest")
    @Mapping(target = "contentData", source = "contentData", qualifiedByName = "mapContentDataRequest")
    void updateEntityFromRequest(TrackEventRequest request, @MappingTarget AnalyticsEvent entity);

    // Custom mapping methods for complex nested objects
    @Named("mapLocation")
    default AnalyticsEventResponse.LocationResponse mapLocation(AnalyticsEvent.Location location) {
        if (location == null) {
            return null;
        }
        AnalyticsEventResponse.LocationResponse response = new AnalyticsEventResponse.LocationResponse();
        response.setCountry(location.getCountry());
        response.setRegion(location.getRegion());
        response.setCity(location.getCity());
        response.setLatitude(location.getLatitude());
        response.setLongitude(location.getLongitude());
        return response;
    }

    @Named("mapPerformanceMetrics")
    default AnalyticsEventResponse.PerformanceMetricsResponse mapPerformanceMetrics(
            AnalyticsEvent.PerformanceMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        AnalyticsEventResponse.PerformanceMetricsResponse response = new AnalyticsEventResponse.PerformanceMetricsResponse();
        response.setResponseTime(metrics.getResponseTime());
        response.setLoadTime(metrics.getLoadTime());
        response.setRenderTime(metrics.getRenderTime());
        response.setMemoryUsage(metrics.getMemoryUsage());
        response.setCpuUsage(metrics.getCpuUsage());
        return response;
    }

    @Named("mapUserContext")
    default AnalyticsEventResponse.UserContextResponse mapUserContext(AnalyticsEvent.UserContext context) {
        if (context == null) {
            return null;
        }
        AnalyticsEventResponse.UserContextResponse response = new AnalyticsEventResponse.UserContextResponse();
        response.setUserSegment(context.getUserSegment());
        response.setUserTier(context.getUserTier());
        response.setIsPremium(context.getIsPremium());
        response.setReferralSource(context.getReferralSource());
        response.setCampaignId(context.getCampaignId());
        return response;
    }

    @Named("mapDeviceContext")
    default AnalyticsEventResponse.DeviceContextResponse mapDeviceContext(AnalyticsEvent.DeviceContext context) {
        if (context == null) {
            return null;
        }
        AnalyticsEventResponse.DeviceContextResponse response = new AnalyticsEventResponse.DeviceContextResponse();
        response.setDeviceModel(context.getDeviceModel());
        response.setOsVersion(context.getOsVersion());
        response.setBrowserVersion(context.getBrowserVersion());
        response.setScreenResolution(context.getScreenResolution());
        response.setNetworkType(context.getNetworkType());
        return response;
    }

    @Named("mapEcommerceData")
    default AnalyticsEventResponse.EcommerceDataResponse mapEcommerceData(AnalyticsEvent.EcommerceData data) {
        if (data == null) {
            return null;
        }
        AnalyticsEventResponse.EcommerceDataResponse response = new AnalyticsEventResponse.EcommerceDataResponse();
        response.setProductId(data.getProductId());
        response.setProductName(data.getProductName());
        response.setCategory(data.getCategory());
        response.setBrand(data.getBrand());
        response.setPrice(data.getPrice() != null ? BigDecimal.valueOf(data.getPrice()) : null);
        response.setCurrency(data.getCurrency());
        response.setQuantity(data.getQuantity());
        response.setTransactionId(data.getTransactionId());
        response.setPaymentMethod(data.getPaymentMethod());
        response.setShippingMethod(data.getShippingMethod());
        response.setTaxAmount(data.getTaxAmount() != null ? BigDecimal.valueOf(data.getTaxAmount()) : null);
        response.setShippingAmount(
                data.getShippingAmount() != null ? BigDecimal.valueOf(data.getShippingAmount()) : null);
        response.setDiscountAmount(
                data.getDiscountAmount() != null ? BigDecimal.valueOf(data.getDiscountAmount()) : null);
        response.setCouponCode(data.getCouponCode());
        response.setCartId(data.getCartId());
        response.setCheckoutStep(data.getCheckoutStep());
        return response;
    }

    @Named("mapSubscriptionData")
    default AnalyticsEventResponse.SubscriptionDataResponse mapSubscriptionData(AnalyticsEvent.SubscriptionData data) {
        if (data == null) {
            return null;
        }
        AnalyticsEventResponse.SubscriptionDataResponse response = new AnalyticsEventResponse.SubscriptionDataResponse();
        response.setSubscriptionId(data.getSubscriptionId());
        response.setPlanId(data.getPlanId());
        response.setPlanName(data.getPlanName());
        response.setPlanType(data.getPlanType());
        response.setPlanPrice(data.getPlanPrice() != null ? BigDecimal.valueOf(data.getPlanPrice()) : null);
        response.setCurrency(data.getCurrency());
        response.setStatus(data.getStatus());
        response.setStartDate(data.getStartDate());
        response.setEndDate(data.getEndDate());
        response.setNextBillingDate(data.getNextBillingDate());
        response.setBillingCycle(data.getBillingCycle());
        response.setPaymentMethod(data.getPaymentMethod());
        response.setAutoRenew(data.getAutoRenew());
        response.setCancellationReason(data.getCancellationReason());
        response.setUpgradeFrom(data.getUpgradeFrom());
        response.setDowngradeTo(data.getDowngradeTo());
        return response;
    }

    @Named("mapSocialData")
    default AnalyticsEventResponse.SocialDataResponse mapSocialData(AnalyticsEvent.SocialData data) {
        if (data == null) {
            return null;
        }
        AnalyticsEventResponse.SocialDataResponse response = new AnalyticsEventResponse.SocialDataResponse();
        response.setPostId(data.getPostId());
        response.setPostType(data.getPostType());
        response.setInteractionType(data.getInteractionType());
        response.setHashtags(stringToList(data.getHashtags()));
        response.setMentions(stringToList(data.getMentions()));
        response.setLocation(data.getLocation());
        response.setMood(data.getMood());
        response.setReach(data.getReach());
        response.setImpressions(data.getImpressions());
        return response;
    }

    @Named("mapContentData")
    default AnalyticsEventResponse.ContentDataResponse mapContentData(AnalyticsEvent.ContentData data) {
        if (data == null) {
            return null;
        }
        AnalyticsEventResponse.ContentDataResponse response = new AnalyticsEventResponse.ContentDataResponse();
        response.setContentType(data.getContentType());
        response.setContentCategory(data.getContentCategory());
        response.setContentTags(stringToList(data.getContentTags()));
        response.setContentLanguage(data.getContentLanguage());
        response.setContentLength(data.getContentLength());
        response.setContentFormat(data.getContentFormat());
        response.setContentQuality(data.getContentQuality());
        response.setContentSource(data.getContentSource());
        response.setIsOriginal(data.getIsOriginal());
        response.setContentStatus(data.getContentStatus());
        return response;
    }

    // Request mapping methods for nested objects
    @Named("mapLocationRequest")
    default AnalyticsEvent.Location mapLocationRequest(TrackEventRequest.LocationRequest location) {
        if (location == null) {
            return null;
        }
        AnalyticsEvent.Location entity = new AnalyticsEvent.Location();
        entity.setCountry(location.getCountry());
        entity.setRegion(location.getRegion());
        entity.setCity(location.getCity());
        entity.setLatitude(location.getLatitude());
        entity.setLongitude(location.getLongitude());
        return entity;
    }

    @Named("mapPerformanceMetricsRequest")
    default AnalyticsEvent.PerformanceMetrics mapPerformanceMetricsRequest(
            TrackEventRequest.PerformanceMetricsRequest metrics) {
        if (metrics == null) {
            return null;
        }
        AnalyticsEvent.PerformanceMetrics entity = new AnalyticsEvent.PerformanceMetrics();
        entity.setResponseTime(metrics.getResponseTime());
        entity.setLoadTime(metrics.getLoadTime());
        entity.setRenderTime(metrics.getRenderTime());
        entity.setMemoryUsage(metrics.getMemoryUsage());
        entity.setCpuUsage(metrics.getCpuUsage());
        return entity;
    }

    @Named("mapUserContextRequest")
    default AnalyticsEvent.UserContext mapUserContextRequest(TrackEventRequest.UserContextRequest context) {
        if (context == null) {
            return null;
        }
        AnalyticsEvent.UserContext entity = new AnalyticsEvent.UserContext();
        entity.setUserSegment(context.getUserSegment());
        entity.setUserTier(context.getUserTier());
        entity.setIsPremium(context.getIsPremium());
        entity.setReferralSource(context.getReferralSource());
        entity.setCampaignId(context.getCampaignId());
        return entity;
    }

    @Named("mapDeviceContextRequest")
    default AnalyticsEvent.DeviceContext mapDeviceContextRequest(TrackEventRequest.DeviceContextRequest context) {
        if (context == null) {
            return null;
        }
        AnalyticsEvent.DeviceContext entity = new AnalyticsEvent.DeviceContext();
        entity.setDeviceModel(context.getDeviceModel());
        entity.setOsVersion(context.getOsVersion());
        entity.setBrowserVersion(context.getBrowserVersion());
        entity.setScreenResolution(context.getScreenResolution());
        entity.setNetworkType(context.getNetworkType());
        return entity;
    }

    @Named("mapEcommerceDataRequest")
    default AnalyticsEvent.EcommerceData mapEcommerceDataRequest(TrackEventRequest.EcommerceDataRequest data) {
        if (data == null) {
            return null;
        }
        AnalyticsEvent.EcommerceData entity = new AnalyticsEvent.EcommerceData();
        entity.setProductId(data.getProductId());
        entity.setProductName(data.getProductName());
        entity.setCategory(data.getCategory());
        entity.setBrand(data.getBrand());
        entity.setPrice(data.getPrice() != null ? data.getPrice().doubleValue() : null);
        entity.setCurrency(data.getCurrency());
        entity.setQuantity(data.getQuantity());
        entity.setTransactionId(data.getTransactionId());
        entity.setPaymentMethod(data.getPaymentMethod());
        entity.setShippingMethod(data.getShippingMethod());
        entity.setTaxAmount(data.getTaxAmount() != null ? data.getTaxAmount().doubleValue() : null);
        entity.setShippingAmount(data.getShippingAmount() != null ? data.getShippingAmount().doubleValue() : null);
        entity.setDiscountAmount(data.getDiscountAmount() != null ? data.getDiscountAmount().doubleValue() : null);
        entity.setCouponCode(data.getCouponCode());
        entity.setCartId(data.getCartId());
        entity.setCheckoutStep(data.getCheckoutStep());
        return entity;
    }

    @Named("mapSubscriptionDataRequest")
    default AnalyticsEvent.SubscriptionData mapSubscriptionDataRequest(TrackEventRequest.SubscriptionDataRequest data) {
        if (data == null) {
            return null;
        }
        AnalyticsEvent.SubscriptionData entity = new AnalyticsEvent.SubscriptionData();
        entity.setSubscriptionId(data.getSubscriptionId());
        entity.setPlanId(data.getPlanId());
        entity.setPlanName(data.getPlanName());
        entity.setPlanType(data.getPlanType());
        entity.setPlanPrice(data.getPlanPrice() != null ? data.getPlanPrice().doubleValue() : null);
        entity.setCurrency(data.getCurrency());
        entity.setStatus(data.getStatus());
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());
        entity.setNextBillingDate(data.getNextBillingDate());
        entity.setBillingCycle(data.getBillingCycle());
        entity.setPaymentMethod(data.getPaymentMethod());
        entity.setAutoRenew(data.getAutoRenew());
        entity.setCancellationReason(data.getCancellationReason());
        entity.setUpgradeFrom(data.getUpgradeFrom());
        entity.setDowngradeTo(data.getDowngradeTo());
        return entity;
    }

    @Named("mapSocialDataRequest")
    default AnalyticsEvent.SocialData mapSocialDataRequest(TrackEventRequest.SocialDataRequest data) {
        if (data == null) {
            return null;
        }
        AnalyticsEvent.SocialData entity = new AnalyticsEvent.SocialData();
        entity.setPostId(data.getPostId());
        entity.setPostType(data.getPostType());
        entity.setInteractionType(data.getInteractionType());
        entity.setHashtags(listToString(data.getHashtags()));
        entity.setMentions(listToString(data.getMentions()));
        entity.setLocation(data.getLocation());
        entity.setMood(data.getMood());
        entity.setReach(data.getReach());
        entity.setImpressions(data.getImpressions());
        return entity;
    }

    @Named("mapContentDataRequest")
    default AnalyticsEvent.ContentData mapContentDataRequest(TrackEventRequest.ContentDataRequest data) {
        if (data == null) {
            return null;
        }
        AnalyticsEvent.ContentData entity = new AnalyticsEvent.ContentData();
        entity.setContentType(data.getContentType());
        entity.setContentCategory(data.getContentCategory());
        entity.setContentTags(listToString(data.getContentTags()));
        entity.setContentLanguage(data.getContentLanguage());
        entity.setContentLength(data.getContentLength());
        entity.setContentFormat(data.getContentFormat());
        entity.setContentQuality(data.getContentQuality());
        entity.setContentSource(data.getContentSource());
        entity.setIsOriginal(data.getIsOriginal());
        entity.setContentStatus(data.getContentStatus());
        return entity;
    }

    // Helper methods for List<String> to String conversion and vice versa
    @Named("listToString")
    default String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(",", list);
    }

    @Named("stringToList")
    default List<String> stringToList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return List.of(str.split(","));
    }

    // EventType mapping method
    @Named("mapEventTypeToEntity")
    default EventType mapEventTypeToEntity(EventType eventType) {
        if (eventType == null) {
            return null;
        }

        // Map the comprehensive EventType enum to the limited AnalyticsEvent.EventType enum
        switch (eventType) {
            // User Events
            case USER_LOGIN:
                return EventType.USER_LOGIN;
            case USER_LOGOUT:
                return EventType.USER_LOGOUT;
            case USER_REGISTRATION:
                return EventType.USER_REGISTRATION;
            case USER_PROFILE_VIEW:
                return EventType.USER_PROFILE_VIEW;
            case USER_PROFILE_UPDATE:
                return EventType.USER_PROFILE_UPDATE;

            // Content Events
            case POST_VIEW:
                return EventType.POST_VIEW;
            case POST_LIKE:
                return EventType.POST_LIKE;
            case POST_UNLIKE:
                return EventType.POST_UNLIKE;
            case POST_SHARE:
                return EventType.POST_SHARE;
            case POST_COMMENT:
                return EventType.POST_COMMENT;
            case POST_CREATE:
                return EventType.POST_CREATE;
            case CONTENT_VIEW:
                return EventType.CONTENT_VIEW;
            case CONTENT_LIKE:
                return EventType.CONTENT_LIKE;
            case CONTENT_UNLIKE:
                return EventType.CONTENT_UNLIKE;
            case CONTENT_SHARE:
                return EventType.CONTENT_SHARE;
            case CONTENT_COMMENT:
                return EventType.CONTENT_COMMENT;
            case CONTENT_CREATE:
                return EventType.CONTENT_CREATE;

            // E-commerce Events
            case PRODUCT_VIEW:
                return EventType.PRODUCT_VIEW;
            case PRODUCT_LIKE:
                return EventType.PRODUCT_LIKE;
            case PRODUCT_UNLIKE:
                return EventType.PRODUCT_UNLIKE;
            case PRODUCT_PURCHASE:
                return EventType.PRODUCT_PURCHASE;
            case PRODUCT_SEARCH:
                return EventType.PRODUCT_SEARCH;
            case CART_ADD:
                return EventType.CART_ADD;
            case CART_REMOVE:
                return EventType.CART_REMOVE;
            case CART_ABANDON:
                return EventType.CART_ABANDON;
            case CHECKOUT_START:
                return EventType.CHECKOUT_START;
            case CHECKOUT_COMPLETE:
                return EventType.CHECKOUT_COMPLETE;
            case PAYMENT_SUCCESS:
                return EventType.PAYMENT_SUCCESS;
            case PAYMENT_FAILED:
                return EventType.PAYMENT_FAILED;
            case REFUND_REQUESTED:
                return EventType.REFUND_REQUESTED;
            case REFUND_PROCESSED:
                return EventType.REFUND_PROCESSED;

            // Subscription Events
            case SUBSCRIPTION_START:
                return EventType.SUBSCRIPTION_START;
            case SUBSCRIPTION_RENEWAL:
                return EventType.SUBSCRIPTION_RENEWAL;
            case SUBSCRIPTION_CANCELLATION:
                return EventType.SUBSCRIPTION_CANCELLATION;
            case SUBSCRIPTION_UPGRADE:
                return EventType.SUBSCRIPTION_UPGRADE;
            case SUBSCRIPTION_DOWNGRADE:
                return EventType.SUBSCRIPTION_DOWNGRADE;
            case SUBSCRIPTION_PAUSE:
                return EventType.SUBSCRIPTION_PAUSE;
            case SUBSCRIPTION_RESUME:
                return EventType.SUBSCRIPTION_RESUME;
            case SUBSCRIPTION_EXPIRED:
                return EventType.SUBSCRIPTION_EXPIRED;
            case SUBSCRIPTION_PAYMENT_FAILED:
                return EventType.SUBSCRIPTION_PAYMENT_FAILED;

            // Chat Events
            case MESSAGE_SENT:
                return EventType.MESSAGE_SENT;
            case CHAT_ROOM_JOIN:
                return EventType.CHAT_ROOM_JOIN;
            case CHAT_ROOM_LEAVE:
                return EventType.CHAT_ROOM_LEAVE;
            case CHAT_STARTED:
                return EventType.CHAT_STARTED;

            // Navigation Events
            case PAGE_VIEW:
                return EventType.PAGE_VIEW;
            case BUTTON_CLICK:
                return EventType.BUTTON_CLICK;
            case SEARCH_PERFORMED:
                return EventType.SEARCH_PERFORMED;
            case FILTER_APPLIED:
                return EventType.FILTER_APPLIED;

            // System Events
            case ERROR_OCCURRED:
                return EventType.ERROR_OCCURRED;
            case PERFORMANCE_METRIC:
                return EventType.PERFORMANCE_METRIC;
            case API_CALL:
                return EventType.API_CALL;
            case CACHE_HIT:
                return EventType.CACHE_HIT;
            case CACHE_MISS:
                return EventType.CACHE_MISS;

            // Default fallback for unmapped events
            default:
                return EventType.USER_LOGIN; // Safe fallback
        }
    }
}
