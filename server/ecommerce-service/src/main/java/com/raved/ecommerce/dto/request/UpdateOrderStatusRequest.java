package com.raved.ecommerce.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for updating order status
 */
@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status is required")
    @NotBlank(message = "Status cannot be blank")
    private String status;

    private String reason;

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
} 