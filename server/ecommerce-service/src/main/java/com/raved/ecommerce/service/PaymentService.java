package com.raved.ecommerce.service;

import com.raved.ecommerce.dto.request.ProcessPaymentRequest;
import com.raved.ecommerce.dto.request.RefundPaymentRequest;
import com.raved.ecommerce.dto.response.PaymentResponse;
import com.raved.ecommerce.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * PaymentService for TheRavedApp
 */
public interface PaymentService {

    /**
     * Process a payment
     */
    PaymentResponse processPayment(ProcessPaymentRequest request);

    /**
     * Refund a payment
     */
    PaymentResponse refundPayment(RefundPaymentRequest request);

    /**
     * Get payment by ID
     */
    PaymentResponse getPayment(String paymentId);

    /**
     * Get payments by user
     */
    Page<PaymentResponse> getPaymentsByUser(Long userId, Pageable pageable);

    /**
     * Get payments by order
     */
    Page<PaymentResponse> getPaymentsByOrder(Long orderId, Pageable pageable);

    /**
     * Get payments by status
     */
    List<PaymentResponse> getPaymentsByStatus(String status);

    /**
     * Get total payments for user
     */
    BigDecimal getTotalPaymentsForUser(Long userId);

    /**
     * Get payment statistics
     */
    Map<String, Object> getPaymentStatistics();

    /**
     * Retry failed payments
     */
    void retryFailedPayments();
}
