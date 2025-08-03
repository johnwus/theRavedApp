package com.raved.notification.service.impl;

import com.raved.notification.service.SmsService;
import com.raved.notification.service.TemplateService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Implementation of SmsService using Twilio
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    @Autowired
    private TemplateService templateService;

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.phone-number:}")
    private String fromPhoneNumber;

    // Statistics tracking
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalDelivered = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        logger.info("Sending SMS to: {}", phoneNumber);
        
        if (!isValidPhoneNumber(phoneNumber)) {
            logger.warn("Invalid phone number format: {}", phoneNumber);
            return false;
        }

        if (!isConfigured()) {
            logger.warn("Twilio not configured, SMS sending disabled");
            return false;
        }

        try {
            initializeTwilio();
            
            Message twilioMessage = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();
            
            totalSent.incrementAndGet();
            
            if ("sent".equals(twilioMessage.getStatus().toString()) || 
                "queued".equals(twilioMessage.getStatus().toString())) {
                totalDelivered.incrementAndGet();
                logger.info("SMS sent successfully to: {}, SID: {}", phoneNumber, twilioMessage.getSid());
                return true;
            } else {
                totalFailed.incrementAndGet();
                logger.warn("SMS failed to send to: {}, Status: {}", phoneNumber, twilioMessage.getStatus());
                return false;
            }
            
        } catch (Exception e) {
            totalFailed.incrementAndGet();
            logger.error("Failed to send SMS to: {}", phoneNumber, e);
            return false;
        }
    }

    @Override
    public boolean sendTemplatedSms(String phoneNumber, String templateName, Map<String, Object> templateData) {
        logger.info("Sending templated SMS using template: {} to: {}", templateName, phoneNumber);
        
        try {
            String message = templateService.processTemplate(templateName, templateData);
            return sendSms(phoneNumber, message);
        } catch (Exception e) {
            logger.error("Failed to process SMS template: {} for: {}", templateName, phoneNumber, e);
            return false;
        }
    }

    @Override
    public void sendBulkSms(List<String> phoneNumbers, String message) {
        logger.info("Sending bulk SMS to {} recipients", phoneNumbers.size());
        
        phoneNumbers.parallelStream().forEach(phoneNumber -> {
            try {
                sendSms(phoneNumber, message);
            } catch (Exception e) {
                logger.error("Failed to send bulk SMS to: {}", phoneNumber, e);
            }
        });
        
        logger.info("Bulk SMS sending completed");
    }

    @Override
    public String sendSmsWithTracking(String phoneNumber, String message) {
        logger.info("Sending SMS with tracking to: {}", phoneNumber);
        
        if (!isValidPhoneNumber(phoneNumber)) {
            logger.warn("Invalid phone number format: {}", phoneNumber);
            return null;
        }

        if (!isConfigured()) {
            logger.warn("Twilio not configured, SMS sending disabled");
            return null;
        }

        try {
            initializeTwilio();
            
            Message twilioMessage = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();
            
            totalSent.incrementAndGet();
            logger.info("SMS sent with tracking to: {}, SID: {}", phoneNumber, twilioMessage.getSid());
            
            return twilioMessage.getSid();
            
        } catch (Exception e) {
            totalFailed.incrementAndGet();
            logger.error("Failed to send SMS with tracking to: {}", phoneNumber, e);
            return null;
        }
    }

    @Override
    public String getSmsDeliveryStatus(String messageId) {
        logger.debug("Getting SMS delivery status for message: {}", messageId);
        
        if (!isConfigured()) {
            logger.warn("Twilio not configured, cannot get delivery status");
            return "UNKNOWN";
        }

        try {
            initializeTwilio();
            Message message = Message.fetcher(messageId).fetch();
            return message.getStatus().toString();
        } catch (Exception e) {
            logger.error("Failed to get SMS delivery status for message: {}", messageId, e);
            return "ERROR";
        }
    }

    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    @Override
    public String formatPhoneNumber(String phoneNumber, String countryCode) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }
        
        // Remove all non-digit characters
        String cleanNumber = phoneNumber.replaceAll("[^\\d]", "");
        
        // Add country code if not present
        if (!cleanNumber.startsWith(countryCode)) {
            cleanNumber = countryCode + cleanNumber;
        }
        
        // Add + prefix if not present
        if (!cleanNumber.startsWith("+")) {
            cleanNumber = "+" + cleanNumber;
        }
        
        return cleanNumber;
    }

    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        logger.info("Sending verification code SMS to: {}", phoneNumber);
        
        String message = String.format("Your verification code is: %s. This code will expire in 10 minutes.", code);
        return sendSms(phoneNumber, message);
    }

    @Override
    public boolean sendOtp(String phoneNumber, String otp, int expiryMinutes) {
        logger.info("Sending OTP SMS to: {}", phoneNumber);
        
        String message = String.format("Your OTP is: %s. This code will expire in %d minutes. Do not share this code with anyone.", 
                otp, expiryMinutes);
        return sendSms(phoneNumber, message);
    }

    @Override
    public SmsStats getSmsStats() {
        return new SmsStats(totalSent.get(), totalDelivered.get(), totalFailed.get());
    }

    private boolean isConfigured() {
        return accountSid != null && !accountSid.isEmpty() && 
               authToken != null && !authToken.isEmpty() && 
               fromPhoneNumber != null && !fromPhoneNumber.isEmpty();
    }

    private void initializeTwilio() {
        if (!Twilio.getUsername().equals(accountSid)) {
            Twilio.init(accountSid, authToken);
        }
    }
}
