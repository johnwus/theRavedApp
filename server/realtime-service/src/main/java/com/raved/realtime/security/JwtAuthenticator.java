package com.raved.realtime.security;

import com.raved.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticator.class);

    @Value("${jwt.secret:mySecretKey}")
    private String jwtSecret;

    public Optional<JwtUser> authenticate(String token) {
        try {
            Claims claims = JwtUtils.parseClaimsWithRawSecret(token, jwtSecret);
            String subject = claims.getSubject();
            Object userId = claims.get("userId");
            JwtUser user = new JwtUser();
            user.setUsername(subject);
            user.setUserId(userId != null ? String.valueOf(userId) : subject);
            return Optional.of(user);
        } catch (Exception ex) {
            logger.debug("JWT validation failed: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public static class JwtUser {
        private String userId;
        private String username;
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}

