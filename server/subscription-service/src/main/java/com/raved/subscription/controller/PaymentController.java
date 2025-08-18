package com.raved.subscription.controller;

import com.raved.subscription.dto.request.ProcessSubscriptionPaymentRequest;
import com.raved.subscription.dto.response.PaymentResponse;
import com.raved.subscription.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Subscription Payment Management
 * Handles payment processing for subscriptions
 */
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Subscription Payments", description = "APIs for managing subscription payments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Process subscription payment")
    @PostMapping("/process")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> processPayment(
            @Valid @RequestBody ProcessSubscriptionPaymentRequest request,
            Authentication authentication) {
        
        logger.info("Processing subscription payment for subscription: {} by user: {}", 
                   request.getSubscriptionId(), authentication.getName());

        try {
            PaymentResponse payment = paymentService.processSubscriptionPayment(request);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Payment processed successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error processing subscription payment: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Payment processing failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Verify payment status")
    @GetMapping("/{paymentId}/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @PathVariable String paymentId,
            Authentication authentication) {
        
        logger.debug("Verifying payment: {} by user: {}", paymentId, authentication.getName());

        try {
            PaymentResponse payment = paymentService.verifyPayment(paymentId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "verified", true
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error verifying payment {}: {}", paymentId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Payment verification failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Process refund")
    @PostMapping("/refund")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> processRefund(
            @Parameter(description = "Subscription ID") @RequestParam Long subscriptionId,
            @Parameter(description = "Refund amount") @RequestParam BigDecimal amount,
            @Parameter(description = "Refund reason") @RequestParam String reason,
            Authentication authentication) {
        
        logger.info("Processing refund for subscription: {} amount: {} by user: {}", 
                   subscriptionId, amount, authentication.getName());

        try {
            PaymentResponse payment = paymentService.processRefund(subscriptionId, amount, reason);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Refund processed successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing refund: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Refund processing failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Retry failed payment")
    @PostMapping("/retry/{subscriptionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> retryPayment(
            @PathVariable Long subscriptionId,
            Authentication authentication) {
        
        logger.info("Retrying payment for subscription: {} by user: {}", 
                   subscriptionId, authentication.getName());

        try {
            // TODO: Add authorization check - user should only retry their own subscription payments
            
            PaymentResponse payment = paymentService.retryPayment(subscriptionId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Payment retry successful"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrying payment for subscription {}: {}", subscriptionId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Payment retry failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get payment history for subscription")
    @GetMapping("/history/{subscriptionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getPaymentHistory(
            @PathVariable Long subscriptionId,
            Authentication authentication) {
        
        logger.debug("Getting payment history for subscription: {} by user: {}", 
                    subscriptionId, authentication.getName());

        try {
            // TODO: Add authorization check - user should only see their own subscription payment history
            
            List<PaymentResponse> payments = paymentService.getPaymentHistory(subscriptionId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payments", payments,
                "count", payments.size()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting payment history for subscription {}: {}", subscriptionId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get payment history: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update payment method for subscription")
    @PutMapping("/payment-method/{subscriptionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updatePaymentMethod(
            @PathVariable Long subscriptionId,
            @Parameter(description = "New payment method ID") @RequestParam String paymentMethodId,
            Authentication authentication) {
        
        logger.info("Updating payment method for subscription: {} to method: {} by user: {}", 
                   subscriptionId, paymentMethodId, authentication.getName());

        try {
            // TODO: Add authorization check - user should only update their own subscription payment method
            
            PaymentResponse payment = paymentService.updatePaymentMethod(subscriptionId, paymentMethodId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Payment method updated successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating payment method for subscription {}: {}", subscriptionId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update payment method: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Cancel recurring payments")
    @PostMapping("/cancel-recurring/{subscriptionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> cancelRecurringPayments(
            @PathVariable Long subscriptionId,
            Authentication authentication) {
        
        logger.info("Cancelling recurring payments for subscription: {} by user: {}", 
                   subscriptionId, authentication.getName());

        try {
            // TODO: Add authorization check - user should only cancel their own subscription payments
            
            paymentService.cancelRecurringPayments(subscriptionId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Recurring payments cancelled successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error cancelling recurring payments for subscription {}: {}", subscriptionId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to cancel recurring payments: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
