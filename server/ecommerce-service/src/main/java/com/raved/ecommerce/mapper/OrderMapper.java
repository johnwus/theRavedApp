package com.raved.ecommerce.mapper;

import com.raved.ecommerce.dto.request.CreateOrderRequest;
import com.raved.ecommerce.dto.response.OrderResponse;
import com.raved.ecommerce.model.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for Order entity and DTOs
 */
@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setBuyerId(order.getBuyerId());
        response.setSellerId(order.getSellerId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setUnitPrice(order.getUnitPrice());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethodId(order.getPaymentMethodId());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        response.setNotes(order.getNotes());
        response.setCancellationReason(order.getCancellationReason());
        response.setRefundReason(order.getRefundReason());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setConfirmedAt(order.getConfirmedAt());
        response.setShippedAt(order.getShippedAt());
        response.setDeliveredAt(order.getDeliveredAt());
        response.setCancelledAt(order.getCancelledAt());

        return response;
    }

    public Order toOrder(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();
        order.setBuyerId(request.getBuyerId());
        order.setSellerId(request.getSellerId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setTotalAmount(request.getTotalAmount());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());

        return order;
    }
}
