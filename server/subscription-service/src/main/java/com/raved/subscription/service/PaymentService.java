package com.raved.subscription.service;

import com.raved.subscription.dto.request.ProcessSubscriptionPaymentRequest;
import com.raved.subscription.dto.response.PaymentResponse;

import java.math.BigDecimal;

/**
 * Service interface for handling subscription payments
 */
public interface PaymentService {

    /**
     * Process payment for a subscription
     */
    PaymentResponse processSubscriptionPayment(ProcessSubscriptionPaymentRequest request);

    /**
     * Process refund for a subscription
     */
    PaymentResponse processRefund(Long subscriptionId, BigDecimal amount, String reason);

    /**
     * Verify payment status with payment gateway
     */
    PaymentResponse verifyPayment(String paymentId);

    /**
     * Handle failed payment
     */
    void handleFailedPayment(Long subscriptionId, String paymentId, String reason);

    /**
     * Retry failed payment
     */
    PaymentResponse retryPayment(Long subscriptionId);

    /**
     * Get payment history for subscription
     */
    java.util.List<PaymentResponse> getPaymentHistory(Long subscriptionId);

    /**
     * Cancel recurring payments for subscription
     */
    void cancelRecurringPayments(Long subscriptionId);

    /**
     * Update payment method for subscription
     */
    PaymentResponse updatePaymentMethod(Long subscriptionId, String paymentMethodId);
}
