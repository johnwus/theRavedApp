package com.raved.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Gateway Configuration for routing and CORS
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**", "/api/auth/**")
                        .uri("lb://user-service"))

                // Event Service Routes (updated from content-service)
                .route("event-service", r -> r.path("/api/events/**", "/api/posts/**", "/api/media/**", "/api/feed/**")
                        .uri("lb://event-service"))

                // Social Service Routes
                .route("social-service", r -> r.path("/api/social/**", "/api/likes/**", "/api/comments/**", "/api/follows/**")
                        .uri("lb://social-service"))

                // Real-time Service Routes
                .route("realtime-service", r -> r.path("/api/chat/**", "/ws/**", "/api/realtime/**")
                        .uri("lb://realtime-service"))

                // E-commerce Service Routes
                .route("ecommerce-service", r -> r.path("/api/store/**", "/api/products/**", "/api/orders/**", "/api/ecommerce/**")
                        .uri("lb://ecommerce-service"))

                // Notification Service Routes
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://notification-service"))

                // Analytics Service Routes
                .route("analytics-service", r -> r.path("/api/analytics/**", "/api/metrics/**")
                        .uri("lb://analytics-service"))

                // Eureka Dashboard Route
                .route("eureka-server", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
