package com.raved.common.util;

import java.text.Normalizer;

public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    public static String truncate(String s, int maxLen) {
        if (s == null || s.length() <= maxLen) return s;
        if (maxLen <= 3) return s.substring(0, Math.max(0, maxLen));
        return s.substring(0, maxLen - 3) + "...";
    }

    public static String slugify(String input) {
        if (isBlank(input)) return input;
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalized.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
