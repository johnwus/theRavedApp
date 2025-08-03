package com.raved.notification.service.impl;

import com.raved.notification.model.Notification;
import com.raved.notification.service.EmailService;
import com.raved.notification.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Implementation of EmailService
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateService templateService;

    @Value("${spring.mail.from:noreply@raved.app}")
    private String fromEmail;

    @Value("${app.name:RAvED}")
    private String appName;

    @Override
    public boolean sendEmail(String to, String subject, String content) {
        logger.info("Sending email to: {}", to);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);
            return false;
        }
    }

    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        logger.info("Sending HTML email to: {}", to);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("HTML email sent successfully to: {}", to);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to: {}", to, e);
            return false;
        }
    }

    @Override
    public boolean sendEmailWithAttachments(String to, String subject, String content, List<MultipartFile> attachments) {
        logger.info("Sending email with {} attachments to: {}", attachments.size(), to);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            
            // Add attachments
            for (MultipartFile attachment : attachments) {
                if (!attachment.isEmpty()) {
                    helper.addAttachment(attachment.getOriginalFilename(), attachment);
                }
            }
            
            mailSender.send(message);
            logger.info("Email with attachments sent successfully to: {}", to);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send email with attachments to: {}", to, e);
            return false;
        }
    }

    @Override
    public boolean sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> templateData) {
        logger.info("Sending templated email using template: {} to: {}", templateName, to);
        
        try {
            String htmlContent = templateService.processTemplate(templateName, templateData);
            return sendHtmlEmail(to, subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send templated email to: {}", to, e);
            return false;
        }
    }

    @Override
    public void sendBulkEmails(List<String> recipients, String subject, String content) {
        logger.info("Sending bulk emails to {} recipients", recipients.size());
        
        recipients.parallelStream().forEach(recipient -> {
            try {
                sendEmail(recipient, subject, content);
            } catch (Exception e) {
                logger.error("Failed to send bulk email to: {}", recipient, e);
            }
        });
        
        logger.info("Bulk email sending completed");
    }

    @Override
    public boolean sendNotificationEmail(Notification notification, String recipientEmail) {
        logger.info("Sending notification email for notification: {} to: {}", notification.getId(), recipientEmail);
        
        if (!isValidEmail(recipientEmail)) {
            logger.warn("Invalid email address: {}", recipientEmail);
            return false;
        }
        
        String subject = notification.getSubject();
        String content = notification.getContent();
        
        // If HTML content is available, use it
        if (notification.getHtmlContent() != null && !notification.getHtmlContent().isEmpty()) {
            return sendHtmlEmail(recipientEmail, subject, notification.getHtmlContent());
        } else {
            return sendEmail(recipientEmail, subject, content);
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean sendWelcomeEmail(String to, String username) {
        logger.info("Sending welcome email to: {}", to);
        
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("username", username);
        templateData.put("appName", appName);
        templateData.put("loginUrl", "https://app.raved.com/login");
        
        return sendTemplatedEmail(to, "Welcome to " + appName + "!", "welcome", templateData);
    }

    @Override
    public boolean sendPasswordResetEmail(String to, String resetToken) {
        logger.info("Sending password reset email to: {}", to);
        
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("resetToken", resetToken);
        templateData.put("resetUrl", "https://app.raved.com/reset-password?token=" + resetToken);
        templateData.put("appName", appName);
        templateData.put("expiryHours", "24");
        
        return sendTemplatedEmail(to, "Password Reset Request", "password-reset", templateData);
    }

    @Override
    public boolean sendEmailVerification(String to, String verificationToken) {
        logger.info("Sending email verification to: {}", to);
        
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("verificationToken", verificationToken);
        templateData.put("verificationUrl", "https://app.raved.com/verify-email?token=" + verificationToken);
        templateData.put("appName", appName);
        
        return sendTemplatedEmail(to, "Verify Your Email Address", "email-verification", templateData);
    }

    @Override
    public boolean sendOrderConfirmationEmail(String to, String orderNumber, Map<String, Object> orderDetails) {
        logger.info("Sending order confirmation email for order: {} to: {}", orderNumber, to);
        
        Map<String, Object> templateData = new HashMap<>(orderDetails);
        templateData.put("orderNumber", orderNumber);
        templateData.put("appName", appName);
        templateData.put("trackingUrl", "https://app.raved.com/orders/" + orderNumber);
        
        return sendTemplatedEmail(to, "Order Confirmation - " + orderNumber, "order-confirmation", templateData);
    }

    @Override
    public boolean sendPromotionalEmail(String to, String promoCode, Map<String, Object> promoDetails) {
        logger.info("Sending promotional email with code: {} to: {}", promoCode, to);
        
        Map<String, Object> templateData = new HashMap<>(promoDetails);
        templateData.put("promoCode", promoCode);
        templateData.put("appName", appName);
        templateData.put("shopUrl", "https://app.raved.com/shop");
        
        return sendTemplatedEmail(to, "Special Offer Just for You!", "promotional", templateData);
    }
}
