package com.raved.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * AnalyticsEvent Document for TheRavedApp MongoDB
 *
 * Represents analytics events for tracking user behavior, ecommerce activities,
 * subscription metrics, and system performance.
 */
@Document(collection = "analytics_events")
@CompoundIndex(name = "idx_user_type_timestamp", def = "{'user_id': 1, 'event_type': 1, 'event_timestamp': -1}")
@CompoundIndex(name = "idx_entity_timestamp", def = "{'entity_type': 1, 'entity_id': 1, 'event_timestamp': -1}")
@CompoundIndex(name = "idx_session_timestamp", def = "{'session_id': 1, 'event_timestamp': -1}")
@CompoundIndex(name = "idx_date_hour", def = "{'event_date': 1, 'event_hour': 1}")
@CompoundIndex(name = "idx_ecommerce_category", def = "{'event_type': 1, 'ecommerce_data.category': 1, 'event_timestamp': -1}")
@CompoundIndex(name = "idx_subscription_status", def = "{'event_type': 1, 'subscription_data.status': 1, 'event_timestamp': -1}")
public class AnalyticsEvent {

    @Id
    private String id;

    @Indexed
    @Field("user_id")
    private String userId; // Reference to user service, nullable for anonymous events

    @Indexed
    @Field("session_id")
    private String sessionId;

    @Indexed
    @Field("event_type")
    private EventType eventType;

    @Size(max = 100, message = "Entity type must not exceed 100 characters")
    @Indexed
    @Field("entity_type")
    private String entityType; // e.g., "post", "product", "user", "subscription"

    @Indexed
    @Field("entity_id")
    private String entityId; // ID of the entity being tracked

    @Field("event_data")
    private Map<String, Object> eventData; // Flexible JSON data for additional event information

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Field("ip_address")
    private String ipAddress;

    @Size(max = 500, message = "User agent must not exceed 500 characters")
    @Field("user_agent")
    private String userAgent;

    @Indexed
    @Size(max = 20, message = "Platform must not exceed 20 characters")
    @Field("platform")
    private String platform; // "web", "ios", "android"

    @Size(max = 100, message = "Device type must not exceed 100 characters")
    @Field("device_type")
    private String deviceType;

    @Indexed
    @Field("event_timestamp")
    private LocalDateTime eventTimestamp;

    @Indexed
    @Field("event_date")
    private String eventDate; // YYYY-MM-DD format for efficient date queries

    @Indexed
    @Field("event_hour")
    private Integer eventHour; // 0-23 for hourly analytics

    @Indexed
    @Field("event_weekday")
    private Integer eventWeekday; // 1-7 for weekly patterns

    @Indexed
    @Field("event_month")
    private Integer eventMonth; // 1-12 for monthly trends

    @Indexed(unique = true, sparse = true)
    @Field("dedup_key")
    private String dedupKey;

    @Field("created_at")
    private LocalDateTime createdAt;

    // Enhanced Analytics Fields
    @Field("location")
    private Location location;

    @Field("performance_metrics")
    private PerformanceMetrics performanceMetrics;

    @Field("user_context")
    private UserContext userContext;

    @Field("device_context")
    private DeviceContext deviceContext;

    // E-commerce Analytics
    @Field("ecommerce_data")
    private EcommerceData ecommerceData;

    // Subscription Analytics
    @Field("subscription_data")
    private SubscriptionData subscriptionData;

    // Social Analytics
    @Field("social_data")
    private SocialData socialData;

    // Content Analytics
    @Field("content_data")
    private ContentData contentData;

    // Note: EventType enum is defined in com.raved.analytics.model.EventType

    // Constructors
    public AnalyticsEvent() {
        this.eventTimestamp = LocalDateTime.now();
        this.eventDate = this.eventTimestamp.toLocalDate().toString();
        this.eventHour = this.eventTimestamp.getHour();
        this.eventWeekday = this.eventTimestamp.getDayOfWeek().getValue();
        this.eventMonth = this.eventTimestamp.getMonthValue();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        if (eventTimestamp != null) {
            this.eventDate = eventTimestamp.toLocalDate().toString();
            this.eventHour = eventTimestamp.getHour();
            this.eventWeekday = eventTimestamp.getDayOfWeek().getValue();
            this.eventMonth = eventTimestamp.getMonthValue();
        }
    }

    public String getDedupKey() {
        return dedupKey;
    }

    public void setDedupKey(String dedupKey) {
        this.dedupKey = dedupKey;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getEventHour() {
        return eventHour;
    }

    public void setEventHour(Integer eventHour) {
        this.eventHour = eventHour;
    }

    public Integer getEventWeekday() {
        return eventWeekday;
    }

    public void setEventWeekday(Integer eventWeekday) {
        this.eventWeekday = eventWeekday;
    }

    public Integer getEventMonth() {
        return eventMonth;
    }

    public void setEventMonth(Integer eventMonth) {
        this.eventMonth = eventMonth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public DeviceContext getDeviceContext() {
        return deviceContext;
    }

    public void setDeviceContext(DeviceContext deviceContext) {
        this.deviceContext = deviceContext;
    }

    public EcommerceData getEcommerceData() {
        return ecommerceData;
    }

    public void setEcommerceData(EcommerceData ecommerceData) {
        this.ecommerceData = ecommerceData;
    }

    public SubscriptionData getSubscriptionData() {
        return subscriptionData;
    }

    public void setSubscriptionData(SubscriptionData subscriptionData) {
        this.subscriptionData = subscriptionData;
    }

    public SocialData getSocialData() {
        return socialData;
    }

    public void setSocialData(SocialData socialData) {
        this.socialData = socialData;
    }

    public ContentData getContentData() {
        return contentData;
    }

    public void setContentData(ContentData contentData) {
        this.contentData = contentData;
    }

    // Inner Classes for Enhanced Analytics
    public static class Location {

        private String country;
        private String region;
        private String city;
        private Double latitude;
        private Double longitude;

        // Getters and setters
        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public static class PerformanceMetrics {

        private Long responseTime;
        private Long loadTime;
        private Long renderTime;
        private Integer memoryUsage;
        private Integer cpuUsage;

        // Getters and setters
        public Long getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(Long responseTime) {
            this.responseTime = responseTime;
        }

        public Long getLoadTime() {
            return loadTime;
        }

        public void setLoadTime(Long loadTime) {
            this.loadTime = loadTime;
        }

        public Long getRenderTime() {
            return renderTime;
        }

        public void setRenderTime(Long renderTime) {
            this.renderTime = renderTime;
        }

        public Integer getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(Integer memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public Integer getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Integer cpuUsage) {
            this.cpuUsage = cpuUsage;
        }
    }

    public static class UserContext {

        private String userSegment;
        private String userTier;
        private Boolean isPremium;
        private String referralSource;
        private String campaignId;

        // Getters and setters
        public String getUserSegment() {
            return userSegment;
        }

        public void setUserSegment(String userSegment) {
            this.userSegment = userSegment;
        }

        public String getUserTier() {
            return userTier;
        }

        public void setUserTier(String userTier) {
            this.userTier = userTier;
        }

        public Boolean getIsPremium() {
            return isPremium;
        }

        public void setIsPremium(Boolean isPremium) {
            this.isPremium = isPremium;
        }

        public String getReferralSource() {
            return referralSource;
        }

        public void setReferralSource(String referralSource) {
            this.referralSource = referralSource;
        }

        public String getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(String campaignId) {
            this.campaignId = campaignId;
        }
    }

    public static class DeviceContext {

        private String deviceModel;
        private String osVersion;
        private String browserVersion;
        private String screenResolution;
        private String networkType;

        // Getters and setters
        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getScreenResolution() {
            return screenResolution;
        }

        public void setScreenResolution(String screenResolution) {
            this.screenResolution = screenResolution;
        }

        public String getNetworkType() {
            return networkType;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }
    }

    public static class EcommerceData {

        private String productId;
        private String productName;
        private String category;
        private String brand;
        private Double price;
        private String currency;
        private Integer quantity;
        private String transactionId;
        private String paymentMethod;
        private String shippingMethod;
        private Double taxAmount;
        private Double shippingAmount;
        private Double discountAmount;
        private String couponCode;
        private String cartId;
        private String checkoutStep;

        // Getters and setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getShippingMethod() {
            return shippingMethod;
        }

        public void setShippingMethod(String shippingMethod) {
            this.shippingMethod = shippingMethod;
        }

        public Double getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(Double taxAmount) {
            this.taxAmount = taxAmount;
        }

        public Double getShippingAmount() {
            return shippingAmount;
        }

        public void setShippingAmount(Double shippingAmount) {
            this.shippingAmount = shippingAmount;
        }

        public Double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(Double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getCheckoutStep() {
            return checkoutStep;
        }

        public void setCheckoutStep(String checkoutStep) {
            this.checkoutStep = checkoutStep;
        }
    }

    public static class SubscriptionData {

        private String subscriptionId;
        private String planId;
        private String planName;
        private String planType; // monthly, yearly, lifetime
        private Double planPrice;
        private String currency;
        private String status; // active, cancelled, paused, expired
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime nextBillingDate;
        private String billingCycle;
        private String paymentMethod;
        private Boolean autoRenew;
        private String cancellationReason;
        private String upgradeFrom;
        private String downgradeTo;

        // Getters and setters
        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public Double getPlanPrice() {
            return planPrice;
        }

        public void setPlanPrice(Double planPrice) {
            this.planPrice = planPrice;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public LocalDateTime getNextBillingDate() {
            return nextBillingDate;
        }

        public void setNextBillingDate(LocalDateTime nextBillingDate) {
            this.nextBillingDate = nextBillingDate;
        }

        public String getBillingCycle() {
            return billingCycle;
        }

        public void setBillingCycle(String billingCycle) {
            this.billingCycle = billingCycle;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public Boolean getAutoRenew() {
            return autoRenew;
        }

        public void setAutoRenew(Boolean autoRenew) {
            this.autoRenew = autoRenew;
        }

        public String getCancellationReason() {
            return cancellationReason;
        }

        public void setCancellationReason(String cancellationReason) {
            this.cancellationReason = cancellationReason;
        }

        public String getUpgradeFrom() {
            return upgradeFrom;
        }

        public void setUpgradeFrom(String upgradeFrom) {
            this.upgradeFrom = upgradeFrom;
        }

        public String getDowngradeTo() {
            return downgradeTo;
        }

        public void setDowngradeTo(String downgradeTo) {
            this.downgradeTo = downgradeTo;
        }
    }

    public static class SocialData {

        private String postId;
        private String postType;
        private String interactionType; // like, comment, share, save
        private String hashtags;
        private String mentions;
        private String location;
        private String mood;
        private Integer reach;
        private Integer impressions;

        // Getters and setters
        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getInteractionType() {
            return interactionType;
        }

        public void setInteractionType(String interactionType) {
            this.interactionType = interactionType;
        }

        public String getHashtags() {
            return hashtags;
        }

        public void setHashtags(String hashtags) {
            this.hashtags = hashtags;
        }

        public String getMentions() {
            return mentions;
        }

        public void setMentions(String mentions) {
            this.mentions = mentions;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMood() {
            return mood;
        }

        public void setMood(String mood) {
            this.mood = mood;
        }

        public Integer getReach() {
            return reach;
        }

        public void setReach(Integer reach) {
            this.reach = reach;
        }

        public Integer getImpressions() {
            return impressions;
        }

        public void setImpressions(Integer impressions) {
            this.impressions = impressions;
        }
    }

    public static class ContentData {

        private String contentType; // post, product, article, video
        private String contentCategory;
        private String contentTags;
        private String contentLanguage;
        private Integer contentLength;
        private String contentFormat;
        private String contentQuality;
        private String contentSource;
        private Boolean isOriginal;
        private String contentStatus; // draft, published, archived

        // Getters and setters
        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentCategory() {
            return contentCategory;
        }

        public void setContentCategory(String contentCategory) {
            this.contentCategory = contentCategory;
        }

        public String getContentTags() {
            return contentTags;
        }

        public void setContentTags(String contentTags) {
            this.contentTags = contentTags;
        }

        public String getContentLanguage() {
            return contentLanguage;
        }

        public void setContentLanguage(String contentLanguage) {
            this.contentLanguage = contentLanguage;
        }

        public Integer getContentLength() {
            return contentLength;
        }

        public void setContentLength(Integer contentLength) {
            this.contentLength = contentLength;
        }

        public String getContentFormat() {
            return contentFormat;
        }

        public void setContentFormat(String contentFormat) {
            this.contentFormat = contentFormat;
        }

        public String getContentQuality() {
            return contentQuality;
        }

        public void setContentQuality(String contentQuality) {
            this.contentQuality = contentQuality;
        }

        public String getContentSource() {
            return contentSource;
        }

        public void setContentSource(String contentSource) {
            this.contentSource = contentSource;
        }

        public Boolean getIsOriginal() {
            return isOriginal;
        }

        public void setIsOriginal(Boolean isOriginal) {
            this.isOriginal = isOriginal;
        }

        public String getContentStatus() {
            return contentStatus;
        }

        public void setContentStatus(String contentStatus) {
            this.contentStatus = contentStatus;
        }
    }
}
