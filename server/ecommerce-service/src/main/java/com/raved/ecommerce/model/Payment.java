package com.raved.ecommerce.model;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment entity for TheRavedApp
 * Based on the payments table schema from migration V4.
 */
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_order", columnList = "order_id"),
        @Index(name = "idx_payments_status", columnList = "status"),
        @Index(name = "idx_payments_transaction", columnList = "transaction_id"),
        @Index(name = "idx_payments_method", columnList = "payment_method")
})
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // MOBILE_MONEY, CARD, BANK_TRANSFER

    @Column(name = "payment_provider")
    private String paymentProvider; // PAYSTACK, FLUTTERWAVE, etc.

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", columnDefinition = "VARCHAR(3) DEFAULT 'GHS'")
    private String currency = "GHS";

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private String status = "PENDING"; // PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED

    @Column(name = "provider_response", columnDefinition = "JSONB")
    private String providerResponse;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "initiated_at", nullable = false, updatable = false)
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    // Additional fields needed by services
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "parent_payment_id")
    private Long parentPaymentId;

    // Constructors
    public Payment() {
    }

    public Payment(Long orderId, String paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    // Lifecycle methods
    @PrePersist
    public void prePersist() {
        this.initiatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public void markAsSuccess(String transactionId) {
        this.status = "SUCCESS";
        this.transactionId = transactionId;
        this.completedAt = LocalDateTime.now();
    }

    public void markAsFailed(String failureReason) {
        this.status = "FAILED";
        this.failureReason = failureReason;
        this.failedAt = LocalDateTime.now();
    }

    public void markAsCancelled() {
        this.status = "CANCELLED";
    }

    public void markAsRefunded() {
        this.status = "REFUNDED";
    }

    public boolean isSuccessful() {
        return "SUCCESS".equals(this.status);
    }

    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public boolean isFailed() {
        return "FAILED".equals(this.status);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getUserId() {
        return buyerId; // Alias for backward compatibility
    }

    public void setUserId(Long userId) {
        this.buyerId = userId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    // Convenience method for services that expect getPaymentId()
    public String getPaymentId() {
        return this.transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProviderResponse() {
        return providerResponse;
    }

    public void setProviderResponse(String providerResponse) {
        this.providerResponse = providerResponse;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(LocalDateTime initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }

    // Additional getters and setters for fields needed by services
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public Long getParentPaymentId() {
        return parentPaymentId;
    }

    public void setParentPaymentId(Long parentPaymentId) {
        this.parentPaymentId = parentPaymentId;
    }

    public void setPaymentId(String paymentId) {
        this.transactionId = paymentId;
    }
}
