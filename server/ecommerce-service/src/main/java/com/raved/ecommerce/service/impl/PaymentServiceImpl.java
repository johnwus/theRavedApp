package com.raved.ecommerce.service.impl;

import com.raved.ecommerce.dto.request.ProcessPaymentRequest;
import com.raved.ecommerce.dto.request.RefundPaymentRequest;
import com.raved.ecommerce.dto.response.PaymentResponse;
import com.raved.ecommerce.event.EcommerceEventPublisher;
import com.raved.ecommerce.exception.PaymentProcessingException;
import com.raved.ecommerce.mapper.PaymentMapper;
import com.raved.ecommerce.model.Order;
import com.raved.ecommerce.model.Payment;
import com.raved.ecommerce.model.PaymentMethod;
import com.raved.ecommerce.model.PaymentStatus;
import com.raved.ecommerce.repository.OrderRepository;
import com.raved.ecommerce.repository.PaymentRepository;
import com.raved.ecommerce.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of PaymentService using Stripe
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EcommerceEventPublisher eventPublisher;

    @Value("${stripe.secret-key:}")
    private String stripeSecretKey;

    @Value("${stripe.publishable-key:}")
    private String stripePublishableKey;

    @PostConstruct
    public void init() {
        if (stripeSecretKey != null && !stripeSecretKey.isEmpty()) {
            Stripe.apiKey = stripeSecretKey;
            logger.info("Stripe payment service initialized");
        } else {
            logger.warn("Stripe secret key not configured - payment processing will be simulated");
        }
    }

    @Override
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        logger.info("Processing payment for order: {} amount: {}", request.getOrderId(), request.getAmount());
        
        // Validate order exists
        Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
        if (orderOpt.isEmpty()) {
            throw new PaymentProcessingException("Order not found: " + request.getOrderId());
        }
        
        Order order = orderOpt.get();
        
        // Create payment record
        Payment payment = new Payment();
        payment.setPaymentId(generatePaymentId());
        payment.setOrderId(request.getOrderId());
        payment.setUserId(order.getUserId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        try {
            // Process payment based on method
            PaymentResult result = processPaymentByMethod(request, savedPayment);
            
            // Update payment with result
            savedPayment.setStatus(result.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
            savedPayment.setTransactionId(result.getTransactionId());
            savedPayment.setGatewayResponse(result.getGatewayResponse());
            savedPayment.setFailureReason(result.getFailureReason());
            savedPayment.setProcessedAt(LocalDateTime.now());
            savedPayment.setUpdatedAt(LocalDateTime.now());
            
            Payment finalPayment = paymentRepository.save(savedPayment);
            
            // Publish payment event
            if (result.isSuccess()) {
                eventPublisher.publishPaymentSuccessEvent(order.getUserId(), request.getOrderId(), 
                        order.getOrderNumber(), request.getAmount(), request.getPaymentMethod().name());
                
                // Update order status
                order.setPaymentStatus(Order.PaymentStatus.PAID);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
            } else {
                eventPublisher.publishPaymentFailedEvent(order.getUserId(), request.getOrderId(), 
                        order.getOrderNumber(), request.getAmount(), result.getFailureReason());
            }
            
            logger.info("Payment processed: {} - Status: {}", finalPayment.getPaymentId(), finalPayment.getStatus());
            return paymentMapper.toPaymentResponse(finalPayment);
            
        } catch (Exception e) {
            logger.error("Payment processing failed for payment: {}", savedPayment.getPaymentId(), e);
            
            // Update payment as failed
            savedPayment.setStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason(e.getMessage());
            savedPayment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(savedPayment);
            
            // Publish failure event
            eventPublisher.publishPaymentFailedEvent(order.getUserId(), request.getOrderId(), 
                    order.getOrderNumber(), request.getAmount(), e.getMessage());
            
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse refundPayment(RefundPaymentRequest request) {
        logger.info("Processing refund for payment: {} amount: {}", request.getPaymentId(), request.getAmount());
        
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentId(request.getPaymentId());
        if (paymentOpt.isEmpty()) {
            throw new PaymentProcessingException("Payment not found: " + request.getPaymentId());
        }
        
        Payment originalPayment = paymentOpt.get();
        
        // Validate payment can be refunded
        if (originalPayment.getStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentProcessingException("Payment cannot be refunded - status: " + originalPayment.getStatus());
        }
        
        BigDecimal refundAmount = request.getAmount() != null ? request.getAmount() : originalPayment.getAmount();
        if (refundAmount.compareTo(originalPayment.getAmount()) > 0) {
            throw new PaymentProcessingException("Refund amount cannot exceed original payment amount");
        }
        
        // Create refund payment record
        Payment refundPayment = new Payment();
        refundPayment.setPaymentId(generatePaymentId());
        refundPayment.setOrderId(originalPayment.getOrderId());
        refundPayment.setUserId(originalPayment.getUserId());
        refundPayment.setAmount(refundAmount.negate()); // Negative amount for refund
        refundPayment.setCurrency(originalPayment.getCurrency());
        refundPayment.setPaymentMethod(originalPayment.getPaymentMethod());
        refundPayment.setStatus(PaymentStatus.PENDING);
        refundPayment.setParentPaymentId(originalPayment.getId());
        refundPayment.setCreatedAt(LocalDateTime.now());
        refundPayment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedRefund = paymentRepository.save(refundPayment);
        
        try {
            // Process refund based on original payment method
            RefundResult result = processRefundByMethod(originalPayment, refundAmount, request.getReason());
            
            // Update refund with result
            savedRefund.setStatus(result.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
            savedRefund.setTransactionId(result.getRefundId());
            savedRefund.setGatewayResponse(result.getGatewayResponse());
            savedRefund.setFailureReason(result.getFailureReason());
            savedRefund.setProcessedAt(LocalDateTime.now());
            savedRefund.setUpdatedAt(LocalDateTime.now());
            
            Payment finalRefund = paymentRepository.save(savedRefund);
            
            // Publish refund event
            if (result.isSuccess()) {
                Optional<Order> orderOpt = orderRepository.findById(originalPayment.getOrderId());
                if (orderOpt.isPresent()) {
                    eventPublisher.publishRefundProcessedEvent(originalPayment.getUserId(), 
                            originalPayment.getOrderId(), orderOpt.get().getOrderNumber(), refundAmount);
                }
            }
            
            logger.info("Refund processed: {} - Status: {}", finalRefund.getPaymentId(), finalRefund.getStatus());
            return paymentMapper.toPaymentResponse(finalRefund);
            
        } catch (Exception e) {
            logger.error("Refund processing failed for payment: {}", savedRefund.getPaymentId(), e);
            
            // Update refund as failed
            savedRefund.setStatus(PaymentStatus.FAILED);
            savedRefund.setFailureReason(e.getMessage());
            savedRefund.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(savedRefund);
            
            throw new PaymentProcessingException("Refund processing failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        logger.debug("Getting payment: {}", paymentId);
        
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentId(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new PaymentProcessingException("Payment not found: " + paymentId);
        }
        
        return paymentMapper.toPaymentResponse(paymentOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByUser(Long userId, Pageable pageable) {
        logger.debug("Getting payments for user: {}", userId);
        
        Page<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return payments.map(paymentMapper::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByOrder(Long orderId, Pageable pageable) {
        logger.debug("Getting payments for order: {}", orderId);
        
        Page<Payment> payments = paymentRepository.findByOrderIdOrderByCreatedAtDesc(orderId, pageable);
        return payments.map(paymentMapper::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        logger.debug("Getting payments by status: {}", status);
        
        List<Payment> payments = paymentRepository.findByStatusOrderByCreatedAtDesc(status);
        return payments.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentsForUser(Long userId) {
        logger.debug("Getting total payments for user: {}", userId);
        
        return paymentRepository.getTotalPaymentAmountByUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPaymentStatistics() {
        logger.debug("Getting payment statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        long totalPayments = paymentRepository.count();
        long successfulPayments = paymentRepository.countByStatus(PaymentStatus.COMPLETED);
        long failedPayments = paymentRepository.countByStatus(PaymentStatus.FAILED);
        long pendingPayments = paymentRepository.countByStatus(PaymentStatus.PENDING);
        
        BigDecimal totalAmount = paymentRepository.getTotalPaymentAmount();
        BigDecimal totalRefunds = paymentRepository.getTotalRefundAmount();
        
        double successRate = totalPayments > 0 ? (double) successfulPayments / totalPayments * 100 : 0;
        
        stats.put("totalPayments", totalPayments);
        stats.put("successfulPayments", successfulPayments);
        stats.put("failedPayments", failedPayments);
        stats.put("pendingPayments", pendingPayments);
        stats.put("successRate", successRate);
        stats.put("totalAmount", totalAmount);
        stats.put("totalRefunds", totalRefunds);
        stats.put("netAmount", totalAmount.subtract(totalRefunds.abs()));
        
        return stats;
    }

    @Override
    public void retryFailedPayments() {
        logger.info("Retrying failed payments");
        
        List<Payment> failedPayments = paymentRepository.findByStatusAndCreatedAtAfter(
                PaymentStatus.FAILED, LocalDateTime.now().minusHours(24));
        
        for (Payment payment : failedPayments) {
            try {
                // Create retry request
                ProcessPaymentRequest retryRequest = new ProcessPaymentRequest();
                retryRequest.setOrderId(payment.getOrderId());
                retryRequest.setAmount(payment.getAmount());
                retryRequest.setCurrency(payment.getCurrency());
                retryRequest.setPaymentMethod(payment.getPaymentMethod());
                
                // Process retry
                processPayment(retryRequest);
                
            } catch (Exception e) {
                logger.error("Failed to retry payment: {}", payment.getPaymentId(), e);
            }
        }
        
        logger.info("Completed retry of {} failed payments", failedPayments.size());
    }

    private PaymentResult processPaymentByMethod(ProcessPaymentRequest request, Payment payment) throws Exception {
        switch (request.getPaymentMethod()) {
            case CREDIT_CARD:
                return processStripePayment(request, payment);
            case DEBIT_CARD:
                return processStripePayment(request, payment);
            case PAYPAL:
                return processPayPalPayment(request, payment);
            case BANK_TRANSFER:
                return processBankTransferPayment(request, payment);
            case DIGITAL_WALLET:
                return processDigitalWalletPayment(request, payment);
            default:
                throw new PaymentProcessingException("Unsupported payment method: " + request.getPaymentMethod());
        }
    }

    private PaymentResult processStripePayment(ProcessPaymentRequest request, Payment payment) throws StripeException {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            // Simulate payment for development
            return simulatePayment(request, payment);
        }
        
        try {
            // Create payment intent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (request.getAmount().doubleValue() * 100)) // Convert to cents
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setDescription("Payment for order: " + request.getOrderId())
                    .putMetadata("orderId", request.getOrderId().toString())
                    .putMetadata("paymentId", payment.getPaymentId())
                    .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            return new PaymentResult(true, paymentIntent.getId(), 
                    "Payment processed successfully", null);
            
        } catch (StripeException e) {
            logger.error("Stripe payment failed", e);
            return new PaymentResult(false, null, null, e.getMessage());
        }
    }

    private PaymentResult processPayPalPayment(ProcessPaymentRequest request, Payment payment) {
        // TODO: Implement PayPal integration
        logger.info("PayPal payment processing not implemented - simulating");
        return simulatePayment(request, payment);
    }

    private PaymentResult processBankTransferPayment(ProcessPaymentRequest request, Payment payment) {
        // TODO: Implement bank transfer processing
        logger.info("Bank transfer processing not implemented - simulating");
        return simulatePayment(request, payment);
    }

    private PaymentResult processDigitalWalletPayment(ProcessPaymentRequest request, Payment payment) {
        // TODO: Implement digital wallet processing
        logger.info("Digital wallet processing not implemented - simulating");
        return simulatePayment(request, payment);
    }

    private PaymentResult simulatePayment(ProcessPaymentRequest request, Payment payment) {
        // Simulate payment processing for development/testing
        boolean success = Math.random() > 0.1; // 90% success rate
        
        if (success) {
            return new PaymentResult(true, "sim_" + UUID.randomUUID().toString(), 
                    "Simulated payment successful", null);
        } else {
            return new PaymentResult(false, null, null, "Simulated payment failure");
        }
    }

    private RefundResult processRefundByMethod(Payment originalPayment, BigDecimal amount, String reason) throws Exception {
        switch (originalPayment.getPaymentMethod()) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                return processStripeRefund(originalPayment, amount, reason);
            case PAYPAL:
                return processPayPalRefund(originalPayment, amount, reason);
            default:
                return simulateRefund(originalPayment, amount, reason);
        }
    }

    private RefundResult processStripeRefund(Payment originalPayment, BigDecimal amount, String reason) throws StripeException {
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            return simulateRefund(originalPayment, amount, reason);
        }
        
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setCharge(originalPayment.getTransactionId())
                    .setAmount((long) (amount.doubleValue() * 100)) // Convert to cents
                    .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                    .putMetadata("originalPaymentId", originalPayment.getPaymentId())
                    .build();
            
            Refund refund = Refund.create(params);
            
            return new RefundResult(true, refund.getId(), 
                    "Refund processed successfully", null);
            
        } catch (StripeException e) {
            logger.error("Stripe refund failed", e);
            return new RefundResult(false, null, null, e.getMessage());
        }
    }

    private RefundResult processPayPalRefund(Payment originalPayment, BigDecimal amount, String reason) {
        // TODO: Implement PayPal refund
        logger.info("PayPal refund processing not implemented - simulating");
        return simulateRefund(originalPayment, amount, reason);
    }

    private RefundResult simulateRefund(Payment originalPayment, BigDecimal amount, String reason) {
        // Simulate refund processing
        boolean success = Math.random() > 0.05; // 95% success rate
        
        if (success) {
            return new RefundResult(true, "ref_" + UUID.randomUUID().toString(), 
                    "Simulated refund successful", null);
        } else {
            return new RefundResult(false, null, null, "Simulated refund failure");
        }
    }

    private String generatePaymentId() {
        return "pay_" + UUID.randomUUID().toString().replace("-", "");
    }

    // Helper classes for payment results
    private static class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String gatewayResponse;
        private final String failureReason;

        public PaymentResult(boolean success, String transactionId, String gatewayResponse, String failureReason) {
            this.success = success;
            this.transactionId = transactionId;
            this.gatewayResponse = gatewayResponse;
            this.failureReason = failureReason;
        }

        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getGatewayResponse() { return gatewayResponse; }
        public String getFailureReason() { return failureReason; }
    }

    private static class RefundResult {
        private final boolean success;
        private final String refundId;
        private final String gatewayResponse;
        private final String failureReason;

        public RefundResult(boolean success, String refundId, String gatewayResponse, String failureReason) {
            this.success = success;
            this.refundId = refundId;
            this.gatewayResponse = gatewayResponse;
            this.failureReason = failureReason;
        }

        public boolean isSuccess() { return success; }
        public String getRefundId() { return refundId; }
        public String getGatewayResponse() { return gatewayResponse; }
        public String getFailureReason() { return failureReason; }
    }
}
