package com.raved.user.util;

import com.raved.security.encryption.EncryptionUtils;
import org.springframework.stereotype.Component;

/**
 * Password encoding utility
 */
@Component
public class PasswordEncoderUtil {

    public String encode(String rawPassword) {
        return EncryptionUtils.hashPassword(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return EncryptionUtils.matchesPassword(rawPassword, encodedPassword);
    }
}
