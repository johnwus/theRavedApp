package com.raved.ecommerce.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO for refunding a payment
 */
@Data
public class RefundPaymentRequest {

    @NotNull(message = "Payment ID is required")
    private String paymentId;

    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String reason;
    
    // Getters and setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
} 