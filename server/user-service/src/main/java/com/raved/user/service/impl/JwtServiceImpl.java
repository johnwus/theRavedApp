package com.raved.user.service.impl;

import com.raved.user.model.User;
import com.raved.user.security.JwtTokenProvider;
import com.raved.user.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of JwtService
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret:mySecretKey}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration:604800000}") // 7 days in milliseconds
    private long refreshExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {
        logger.debug("Generating JWT token for user: {}", user.getUsername());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("isVerified", user.getIsVerified());
        claims.put("facultyId", user.getFacultyId());
        claims.put("universityId", user.getUniversityId());
        
        return createToken(claims, user.getUsername(), jwtExpirationMs);
    }

    @Override
    public String generateToken(Authentication authentication) {
        logger.debug("Generating JWT token for authentication: {}", authentication.getName());
        
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);
        
        return createToken(claims, authentication.getName(), jwtExpirationMs);
    }

    @Override
    public String generateRefreshToken(User user) {
        logger.debug("Generating refresh token for user: {}", user.getUsername());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("tokenType", "refresh");
        
        return createToken(claims, user.getUsername(), refreshExpirationMs);
    }

    @Override
    public String generateTokenWithCustomExpiration(User user, long expirationMs) {
        logger.debug("Generating JWT token with custom expiration for user: {}", user.getUsername());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        
        return createToken(claims, user.getUsername(), expirationMs);
    }

    @Override
    public String getUsernameFromToken(String token) {
        logger.debug("Extracting username from token");
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            return null;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        logger.debug("Extracting user ID from token");
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            }
            return null;
        } catch (Exception e) {
            logger.error("Error extracting user ID from token", e);
            return null;
        }
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        logger.debug("Extracting expiration date from token");
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token", e);
            return null;
        }
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        logger.debug("Extracting all claims from token");
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Error extracting claims from token", e);
            return null;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        logger.debug("Checking if token is expired");
        
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            logger.error("Error checking token expiration", e);
            return true;
        }
    }

    @Override
    public boolean validateToken(String token) {
        logger.debug("Validating JWT token");
        
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token compact of handler are invalid: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("JWT token validation error", e);
            return false;
        }
    }

    @Override
    public boolean validateToken(String token, String username) {
        logger.debug("Validating JWT token for username: {}", username);
        
        try {
            String tokenUsername = getUsernameFromToken(token);
            return username.equals(tokenUsername) && validateToken(token);
        } catch (Exception e) {
            logger.error("Error validating token for username: {}", username, e);
            return false;
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        logger.debug("Refreshing JWT token");
        
        try {
            if (!validateToken(refreshToken)) {
                logger.warn("Invalid refresh token");
                return null;
            }
            
            Claims claims = getAllClaimsFromToken(refreshToken);
            if (claims == null) {
                logger.warn("Cannot extract claims from refresh token");
                return null;
            }
            
            String tokenType = (String) claims.get("tokenType");
            if (!"refresh".equals(tokenType)) {
                logger.warn("Token is not a refresh token");
                return null;
            }
            
            String username = claims.getSubject();
            Long userId = getUserIdFromToken(refreshToken);
            
            // Create new access token
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put("userId", userId);
            
            return createToken(newClaims, username, jwtExpirationMs);
        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            return null;
        }
    }

    @Override
    public LocalDateTime getTokenExpirationAsLocalDateTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration != null) {
            return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    @Override
    public long getTokenRemainingTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration != null) {
            return expiration.getTime() - System.currentTimeMillis();
        }
        return 0;
    }

    @Override
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            if (claims != null) {
                String tokenType = (String) claims.get("tokenType");
                return "refresh".equals(tokenType);
            }
        } catch (Exception e) {
            logger.error("Error checking if token is refresh token", e);
        }
        return false;
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
