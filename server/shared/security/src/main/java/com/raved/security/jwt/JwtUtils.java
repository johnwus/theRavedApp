package com.raved.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Minimal JWT utilities: build and parse HS256 tokens.
 */
public final class JwtUtils {

    private JwtUtils() {}

    public static String generateToken(String subject, Map<String, Object> claims, String base64Secret, long expiresInMs) {
        Key key = decodeHmacKey(base64Secret);
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expiresInMs)))
                .signWith(key)
                .compact();
    }

    public static Claims parseClaims(String token, String base64Secret) {
        Key key = decodeHmacKey(base64Secret);
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Key decodeHmacKey(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ---- Raw secret helpers (non-base64) ----
    public static Key hmacKeyFromRaw(String rawSecret) {
        return Keys.hmacShaKeyFor(rawSecret.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateTokenWithRawSecret(String subject, Map<String, Object> claims, String rawSecret, long expiresInMs) {
        Key key = hmacKeyFromRaw(rawSecret);
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expiresInMs)))
                .signWith(key)
                .compact();
    }

    public static Claims parseClaimsWithRawSecret(String token, String rawSecret) {
        Key key = hmacKeyFromRaw(rawSecret);
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
