package com.raved.common.util;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private ValidationUtils() {}

    public static boolean isEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }

    public static void requireNotBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireLengthBetween(String value, int min, int max, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        int len = value.length();
        if (len < min || len > max) {
            throw new IllegalArgumentException(message);
        }
    }
}
