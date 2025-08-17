package com.raved.social.util;

/**
 * Utility class for converting between Long and String IDs for MongoDB compatibility
 */
public class MongoIdConverter {
    
    /**
     * Safely converts Long ID to String for MongoDB compatibility
     * @param id Long ID to convert
     * @return String representation of the ID, or null if input is null
     */
    public static String toStringId(Long id) {
        return id != null ? id.toString() : null;
    }
    
    /**
     * Safely converts String ID to Long for backward compatibility
     * @param id String ID to convert
     * @return Long representation of the ID, or null if input is null or invalid
     */
    public static Long toLongId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
