package com.raved.ecommerce.mapper;

import com.raved.ecommerce.dto.response.PaymentResponse;
import com.raved.ecommerce.model.Payment;
import org.springframework.stereotype.Component;

/**
 * Mapper for Payment entity and DTOs
 */
@Component
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setPaymentId(payment.getPaymentId());
        response.setOrderId(payment.getOrderId());
        response.setUserId(payment.getUserId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setGatewayResponse(payment.getGatewayResponse());
        response.setFailureReason(payment.getFailureReason());
        response.setParentPaymentId(payment.getParentPaymentId());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setProcessedAt(payment.getProcessedAt());

        return response;
    }
} 