package com.raved.subscription.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for payment operations
 */
public class PaymentResponse {

    private String paymentId;
    private Long subscriptionId;
    private BigDecimal amount;
    private String currency = "USD";
    private String status; // PENDING, COMPLETED, FAILED, REFUNDED, CANCELLED
    private String description;
    private Instant transactionDate;
    private String paymentMethodId;
    private String gatewayTransactionId;
    private String failureReason;
    private String receiptUrl;

    // Constructors
    public PaymentResponse() {}

    public PaymentResponse(String paymentId, Long subscriptionId, BigDecimal amount, String status) {
        this.paymentId = paymentId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.status = status;
        this.transactionDate = Instant.now();
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "paymentId='" + paymentId + '\'' +
                ", subscriptionId=" + subscriptionId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", gatewayTransactionId='" + gatewayTransactionId + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                '}';
    }
}
