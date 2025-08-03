package com.raved.notification.service;

import com.raved.notification.model.Notification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * EmailService for TheRavedApp
 */
public interface EmailService {

    /**
     * Send simple email
     */
    boolean sendEmail(String to, String subject, String content);

    /**
     * Send email with HTML content
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * Send email with attachments
     */
    boolean sendEmailWithAttachments(String to, String subject, String content, List<MultipartFile> attachments);

    /**
     * Send templated email
     */
    boolean sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> templateData);

    /**
     * Send bulk emails
     */
    void sendBulkEmails(List<String> recipients, String subject, String content);

    /**
     * Send notification via email
     */
    boolean sendNotificationEmail(Notification notification, String recipientEmail);

    /**
     * Validate email address
     */
    boolean isValidEmail(String email);

    /**
     * Send welcome email
     */
    boolean sendWelcomeEmail(String to, String username);

    /**
     * Send password reset email
     */
    boolean sendPasswordResetEmail(String to, String resetToken);

    /**
     * Send email verification
     */
    boolean sendEmailVerification(String to, String verificationToken);

    /**
     * Send order confirmation email
     */
    boolean sendOrderConfirmationEmail(String to, String orderNumber, Map<String, Object> orderDetails);

    /**
     * Send promotional email
     */
    boolean sendPromotionalEmail(String to, String promoCode, Map<String, Object> promoDetails);
}
