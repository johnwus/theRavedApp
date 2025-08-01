package com.raved.content.validator;

import com.raved.content.model.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Content validation service for posts and media
 */
@Component
public class ContentValidator {

    /**
     * Validates content based on its type
     */
    public ValidationResult validateContent(ContentType contentType, String content, List<MultipartFile> mediaFiles) {
        ValidationResult result = new ValidationResult();

        // Validate content length
        if (content != null && content.length() > contentType.getMaxContentLength()) {
            result.addError("Content exceeds maximum length of " + contentType.getMaxContentLength() + " characters");
        }

        // Validate media requirements
        if (contentType.isRequiresMedia() && (mediaFiles == null || mediaFiles.isEmpty())) {
            result.addError("Content type " + contentType.getCode() + " requires media files");
        }

        // Validate media count
        if (mediaFiles != null && mediaFiles.size() > contentType.getMaxMediaCount()) {
            result.addError("Too many media files. Maximum allowed: " + contentType.getMaxMediaCount());
        }

        // Validate media types
        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            String[] allowedTypes = contentType.getAllowedMediaTypes();
            for (MultipartFile file : mediaFiles) {
                if (!isValidMediaType(file, allowedTypes)) {
                    result.addError("Invalid media type for file: " + file.getOriginalFilename());
                }
            }
        }

        // Content-specific validations
        switch (contentType) {
            case LINK:
                validateLinkContent(content, result);
                break;
            case POLL:
                validatePollContent(content, result);
                break;
            case EVENT:
                validateEventContent(content, result);
                break;
            case PRODUCT:
                validateProductContent(content, result);
                break;
        }

        return result;
    }

    /**
     * Validates if media file type is allowed
     */
    private boolean isValidMediaType(MultipartFile file, String[] allowedTypes) {
        if (file.getOriginalFilename() == null) {
            return false;
        }

        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        return Arrays.asList(allowedTypes).contains(extension);
    }

    /**
     * Gets file extension from filename
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * Validates link content
     */
    private void validateLinkContent(String content, ValidationResult result) {
        if (content == null || content.trim().isEmpty()) {
            result.addError("Link content cannot be empty");
            return;
        }

        // Basic URL validation
        if (!content.matches("^https?://.*")) {
            result.addError("Link must start with http:// or https://");
        }
    }

    /**
     * Validates poll content
     */
    private void validatePollContent(String content, ValidationResult result) {
        if (content == null || content.trim().isEmpty()) {
            result.addError("Poll question cannot be empty");
            return;
        }

        // Poll content should contain question and options
        if (!content.contains("?")) {
            result.addError("Poll must contain a question");
        }
    }

    /**
     * Validates event content
     */
    private void validateEventContent(String content, ValidationResult result) {
        if (content == null || content.trim().isEmpty()) {
            result.addError("Event description cannot be empty");
            return;
        }

        // Event should have basic information
        String lowerContent = content.toLowerCase();
        if (!lowerContent.contains("date") && !lowerContent.contains("time")) {
            result.addWarning("Event should include date and time information");
        }
    }

    /**
     * Validates product content
     */
    private void validateProductContent(String content, ValidationResult result) {
        if (content == null || content.trim().isEmpty()) {
            result.addError("Product description cannot be empty");
            return;
        }

        // Product should have price information
        String lowerContent = content.toLowerCase();
        if (!lowerContent.contains("price") && !lowerContent.contains("$") && !lowerContent.contains("cost")) {
            result.addWarning("Product should include price information");
        }
    }

    /**
     * Validates file size
     */
    public boolean isValidFileSize(MultipartFile file, long maxSizeBytes) {
        return file.getSize() <= maxSizeBytes;
    }

    /**
     * Checks if content contains profanity (basic implementation)
     */
    public boolean containsProfanity(String content) {
        if (content == null) {
            return false;
        }

        // Basic profanity filter - in production, use a proper service
        String[] profanityWords = {"spam", "scam", "fake"};
        String lowerContent = content.toLowerCase();
        
        for (String word : profanityWords) {
            if (lowerContent.contains(word)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Validation result class
     */
    public static class ValidationResult {
        private boolean valid = true;
        private java.util.List<String> errors = new java.util.ArrayList<>();
        private java.util.List<String> warnings = new java.util.ArrayList<>();

        public void addError(String error) {
            this.valid = false;
            this.errors.add(error);
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
    }
}
