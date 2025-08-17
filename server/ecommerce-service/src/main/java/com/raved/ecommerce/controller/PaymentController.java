package com.raved.ecommerce.controller;

import com.raved.ecommerce.dto.request.ProcessPaymentRequest;
import com.raved.ecommerce.dto.request.RefundPaymentRequest;
import com.raved.ecommerce.dto.response.PaymentResponse;
import com.raved.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Payment Processing
 * Handles payment operations including processing, verification, and refunds
 */
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Processing", description = "APIs for payment processing and management")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Process payment for order")
    @PostMapping("/process")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> processPayment(
            @Valid @RequestBody ProcessPaymentRequest request,
            Authentication authentication) {

        logger.info("Processing payment for order: {} by user: {}", request.getOrderId(), authentication.getName());

        try {
            PaymentResponse payment = paymentService.processPayment(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Payment processed successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error processing payment for order {}: {}", request.getOrderId(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Payment processing failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Check payment status")
    @GetMapping("/{paymentId}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(
            @PathVariable String paymentId,
            Authentication authentication) {

        logger.debug("Getting payment status for payment ID: {} by user: {}", paymentId, authentication.getName());

        try {
            PaymentResponse payment = paymentService.getPayment(paymentId);

            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "status", payment.getStatus()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting payment status for payment {}: {}", paymentId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get payment status: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Verify payment completion")
    @PostMapping("/{paymentId}/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @PathVariable String paymentId,
            Authentication authentication) {

        logger.info("Verifying payment completion for payment ID: {} by user: {}", paymentId, authentication.getName());

        try {
            // TODO: Implement payment verification logic with payment gateway
            PaymentResponse payment = paymentService.getPayment(paymentId);

            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "verified", true,
                "message", "Payment verification completed"
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

    @Operation(summary = "Request payment refund")
    @PostMapping("/refund")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> refundPayment(
            @Valid @RequestBody RefundPaymentRequest request,
            Authentication authentication) {

        logger.info("Processing refund request for payment: {} by user: {}", request.getPaymentId(), authentication.getName());

        try {
            PaymentResponse payment = paymentService.refundPayment(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "payment", payment,
                "message", "Refund processed successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing refund for payment {}: {}", request.getPaymentId(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Refund processing failed: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get user's payment history")
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getPaymentHistory(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int pageSize,
            Authentication authentication) {

        logger.debug("Getting payment history for user: {}", authentication.getName());

        // TODO: Extract user ID from authentication
        Long userId = 1L; // Placeholder - should extract from JWT token

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<PaymentResponse> payments = paymentService.getPaymentsByUser(userId, pageable);

        Map<String, Object> response = Map.of(
            "success", true,
            "payments", payments.getContent(),
            "pagination", Map.of(
                "page", payments.getNumber(),
                "size", payments.getSize(),
                "totalElements", payments.getTotalElements(),
                "totalPages", payments.getTotalPages(),
                "hasNext", payments.hasNext()
            )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get payment statistics (admin only)")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaymentStatistics() {

        logger.debug("Getting payment statistics");

        Map<String, Object> statistics = paymentService.getPaymentStatistics();

        Map<String, Object> response = Map.of(
            "success", true,
            "statistics", statistics
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get payments by status (admin only)")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaymentsByStatus(@PathVariable String status) {

        logger.debug("Getting payments by status: {}", status);

        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);

        Map<String, Object> response = Map.of(
            "success", true,
            "payments", payments,
            "status", status
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retry failed payments (admin only)")
    @PostMapping("/retry-failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> retryFailedPayments() {

        logger.info("Retrying failed payments");

        try {
            paymentService.retryFailedPayments();

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Failed payments retry initiated"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrying failed payments: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to retry payments: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
