package com.raved.user.web;

import com.raved.user.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exposes a minimal JWKS endpoint for HMAC (oct) keys so that API Gateway
 * can validate JWTs using spring.security.oauth2.resourceserver.jwt.jwk-set-uri
 */
@RestController
public class JwksController {

    @Autowired
    private JwtConfig jwtConfig;

    @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> jwks() {
        String secret = jwtConfig.getSecret();
        String base64UrlSecret = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(secret.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> jwk = new HashMap<>();
        jwk.put("kty", "oct");          // HMAC symmetric key
        jwk.put("k", base64UrlSecret);    // base64url encoded secret
        jwk.put("alg", "HS256");        // align with signing algorithm
        jwk.put("use", "sig");          // signature use
        jwk.put("kid", deriveKid(secret));

        return Map.of("keys", List.of(jwk));
    }

    private String deriveKid(String secret) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest).substring(0, 16);
        } catch (NoSuchAlgorithmException e) {
            return "default-kid";
        }
    }
}


