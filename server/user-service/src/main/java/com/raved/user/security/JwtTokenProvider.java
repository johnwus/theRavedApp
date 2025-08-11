package com.raved.user.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.raved.user.config.JwtConfig;
import com.raved.user.model.User;

import io.jsonwebtoken.Claims;
import com.raved.security.jwt.JwtUtils;

/**
 * JWT Token Provider
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private JwtConfig jwtConfig;

    // Access token validity (24 hours)
    private static final long ACCESS_TOKEN_VALIDITY = 24 * 60 * 60 * 1000L;

    // Refresh token validity (7 days)
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return JwtUtils.parseClaimsWithRawSecret(token, jwtConfig.getSecret());
        } catch (Exception e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        claims.put("type", "access");

        return generateToken(claims, user.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("type", "refresh");

        return generateToken(claims, user.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    private String generateToken(Map<String, Object> claims, String subject, long validity) {
        return JwtUtils.generateTokenWithRawSecret(
                subject,
                claims,
                jwtConfig.getSecret(),
                validity
        );
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public long getAccessTokenValidityInSeconds() {
        return ACCESS_TOKEN_VALIDITY / 1000;
    }

    public long getRefreshTokenValidityInSeconds() {
        return REFRESH_TOKEN_VALIDITY / 1000;
    }

    // Signing key now handled by shared JwtUtils
}
