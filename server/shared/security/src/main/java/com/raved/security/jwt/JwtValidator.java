package com.raved.security.jwt;

import io.jsonwebtoken.Claims;

/**
 * Simple JWT validator using JwtUtils.
 */
public final class JwtValidator {

    private JwtValidator() {}

    public static boolean isValid(String token, String base64Secret) {
        try {
            Claims claims = JwtUtils.parseClaims(token, base64Secret);
            return claims.getExpiration() == null || claims.getExpiration().getTime() > System.currentTimeMillis();
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getSubject(String token, String base64Secret) {
        Claims claims = JwtUtils.parseClaims(token, base64Secret);
        return claims.getSubject();
    }
}
