package com.raved.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${raved.gateway.services.user-service.url:lb://user-service}")
    private String userServiceUrl;

    @Value("${raved.gateway.services.content-service.url:lb://content-service}")
    private String contentServiceUrl;

    @Value("${raved.gateway.services.social-service.url:lb://social-service}")
    private String socialServiceUrl;

    @Value("${raved.gateway.services.realtime-service.url:lb://realtime-service}")
    private String realtimeServiceUrl;

    @Value("${raved.gateway.services.ecommerce-service.url:lb://ecommerce-service}")
    private String ecommerceServiceUrl;

    @Value("${raved.gateway.services.notification-service.url:lb://notification-service}")
    private String notificationServiceUrl;

    @Value("${raved.gateway.services.analytics-service.url:lb://analytics-service}")
    private String analyticsServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/api/auth/**", "/api/users/**")
                .uri(userServiceUrl))
            .route("content-service", r -> r.path("/api/posts/**", "/api/media/**", "/api/feed/**", "/api/events/**")
                .uri(contentServiceUrl))
            .route("social-service", r -> r.path("/api/social/**", "/api/likes/**", "/api/comments/**", "/api/follows/**")
                .uri(socialServiceUrl))
            .route("realtime-service-ws", r -> r.path("/ws/**")
                .uri(realtimeServiceUrl))
            .route("realtime-service-api", r -> r.path("/api/chat/**", "/api/realtime/**")
                .uri(realtimeServiceUrl))
            .route("ecommerce-service", r -> r.path("/api/store/**", "/api/products/**", "/api/orders/**", "/api/ecommerce/**", "/api/payments/**")
                .uri(ecommerceServiceUrl))
            .route("notification-service", r -> r.path("/api/notifications/**")
                .uri(notificationServiceUrl))
            .route("analytics-service", r -> r.path("/api/analytics/**", "/api/metrics/**")
                .uri(analyticsServiceUrl))
            .build();
    }
}
