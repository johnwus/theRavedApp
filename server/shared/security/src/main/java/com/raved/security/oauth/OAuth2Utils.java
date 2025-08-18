package com.raved.security.oauth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * Small OAuth2 helpers for resource servers/clients.
 */
public final class OAuth2Utils {

    private OAuth2Utils() {}

    public static Optional<String> extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return Optional.empty();
        }
        String prefix = "Bearer ";
        if (authorizationHeader.regionMatches(true, 0, prefix, 0, prefix.length())) {
            String token = authorizationHeader.substring(prefix.length()).trim();
            if (!token.isEmpty()) {
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    public static HttpHeaders buildWwwAuthenticateHeader(String realm, String error, String errorDescription) {
        StringBuilder value = new StringBuilder("Bearer realm=\"").append(realm).append("\"");
        if (error != null) {
            value.append(", error=\"").append(error).append("\"");
        }
        if (errorDescription != null) {
            value.append(", error_description=\"").append(errorDescription).append("\"");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.WWW_AUTHENTICATE, value.toString());
        return headers;
    }

    public static ResponseEntity.BodyBuilder unauthorized(String realm, String error, String description) {
        return ResponseEntity.status(401).headers(buildWwwAuthenticateHeader(realm, error, description));
    }
}
