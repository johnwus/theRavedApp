package com.raved.common.constants;

/**
 * API constants for TheRavedApp
 */
public final class ApiConstants {

    private ApiConstants() {
        // Utility class
    }

    // API Versions
    public static final String API_V1 = "/api/v1";
    public static final String API_V2 = "/api/v2";

    // Common Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CONTENT_TYPE_JSON = "application/json";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    public static final String DEFAULT_SORT_BY = "createdAt";

    // Cache Keys
    public static final String CACHE_USER_PREFIX = "user:";
    public static final String CACHE_POST_PREFIX = "post:";
    public static final String CACHE_FEED_PREFIX = "feed:";

    // Rate Limiting
    public static final int DEFAULT_RATE_LIMIT = 100;
    public static final int AUTH_RATE_LIMIT = 10;

    // File Upload
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    public static final String[] ALLOWED_IMAGE_TYPES = { "jpg", "jpeg", "png", "gif", "webp" };
    public static final String[] ALLOWED_VIDEO_TYPES = { "mp4", "avi", "mov", "wmv" };
    public static final String[] ALLOWED_DOCUMENT_TYPES = { "pdf", "doc", "docx", "txt" };

    // Service Names
    public static final String USER_SERVICE = "user-service";
    public static final String CONTENT_SERVICE = "content-service";
    public static final String SOCIAL_SERVICE = "social-service";
    public static final String REALTIME_SERVICE = "realtime-service";
    public static final String ECOMMERCE_SERVICE = "ecommerce-service";
    public static final String NOTIFICATION_SERVICE = "notification-service";
    public static final String ANALYTICS_SERVICE = "analytics-service";

    // Database Constants
    public static final String DEFAULT_SCHEMA = "raved";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    public static final int DEFAULT_QUERY_TIMEOUT = 60000;

    // Security Constants
    public static final String JWT_SECRET_KEY = "jwt.secret";
    public static final long JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String MODERATOR_ROLE = "MODERATOR";

    // Notification Constants
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String SMS_QUEUE = "sms.queue";
    public static final String PUSH_QUEUE = "push.queue";

    // Analytics Constants
    public static final String ANALYTICS_QUEUE = "analytics.queue";
    public static final String METRICS_CACHE_PREFIX = "metrics:";
    public static final int ANALYTICS_BATCH_SIZE = 100;

    // Content Moderation
    public static final String MODERATION_QUEUE = "moderation.queue";
    public static final int MAX_CONTENT_LENGTH = 5000;
    public static final int MAX_COMMENT_LENGTH = 2000;

    // E-commerce Constants
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String INVENTORY_CACHE_PREFIX = "inventory:";
    public static final int MAX_CART_ITEMS = 50;
    public static final int ORDER_TIMEOUT_MINUTES = 30;
}
