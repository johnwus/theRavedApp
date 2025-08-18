package com.raved.user.service;

import com.raved.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * JwtService for TheRavedApp
 */
public interface JwtService {
    
    String generateToken(User user);
    
    String generateRefreshToken(User user);
    
    String extractUsername(String token);
    
    boolean isTokenValid(String token, UserDetails userDetails);
    
    boolean isTokenExpired(String token);
    
    String refreshToken(String refreshToken);
    
    void invalidateToken(String token);
    
    void invalidateAllUserTokens(Long userId);
    
    Long extractUserId(String token);
    
    String extractRole(String token);
    
    boolean validateRefreshToken(String refreshToken);
    
    Long getTokenExpirationTime(String token);
    
    boolean isRefreshTokenExpired(String refreshToken);

    // Added to match implementation
    long getTokenRemainingTime(String token);
}
