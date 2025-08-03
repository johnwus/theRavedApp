package com.raved.notification.service;

import java.util.List;
import java.util.Map;

/**
 * SmsService for TheRavedApp
 */
public interface SmsService {

    /**
     * Send SMS to a single recipient
     */
    boolean sendSms(String phoneNumber, String message);

    /**
     * Send SMS with template
     */
    boolean sendTemplatedSms(String phoneNumber, String templateName, Map<String, Object> templateData);

    /**
     * Send bulk SMS
     */
    void sendBulkSms(List<String> phoneNumbers, String message);

    /**
     * Send SMS with delivery tracking
     */
    String sendSmsWithTracking(String phoneNumber, String message);

    /**
     * Get SMS delivery status
     */
    String getSmsDeliveryStatus(String messageId);

    /**
     * Validate phone number format
     */
    boolean isValidPhoneNumber(String phoneNumber);

    /**
     * Format phone number to international format
     */
    String formatPhoneNumber(String phoneNumber, String countryCode);

    /**
     * Send verification code SMS
     */
    boolean sendVerificationCode(String phoneNumber, String code);

    /**
     * Send OTP SMS
     */
    boolean sendOtp(String phoneNumber, String otp, int expiryMinutes);

    /**
     * Get SMS sending statistics
     */
    SmsStats getSmsStats();

    /**
     * Inner class for SMS statistics
     */
    class SmsStats {
        private long totalSent;
        private long totalDelivered;
        private long totalFailed;
        private double deliveryRate;

        public SmsStats() {}

        public SmsStats(long totalSent, long totalDelivered, long totalFailed) {
            this.totalSent = totalSent;
            this.totalDelivered = totalDelivered;
            this.totalFailed = totalFailed;
            this.deliveryRate = totalSent > 0 ? (double) totalDelivered / totalSent * 100 : 0;
        }

        // Getters and setters
        public long getTotalSent() { return totalSent; }
        public void setTotalSent(long totalSent) { this.totalSent = totalSent; }

        public long getTotalDelivered() { return totalDelivered; }
        public void setTotalDelivered(long totalDelivered) { this.totalDelivered = totalDelivered; }

        public long getTotalFailed() { return totalFailed; }
        public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }

        public double getDeliveryRate() { return deliveryRate; }
        public void setDeliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; }
    }
}
