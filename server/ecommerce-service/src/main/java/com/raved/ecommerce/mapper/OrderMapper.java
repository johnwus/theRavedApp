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
        response.setBuyerId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setOrderNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }

    public Order toOrder(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();
        order.setUserId(request.getBuyerId());
        order.setSellerId(request.getSellerId());
        order.setTotalAmount(request.getTotalAmount());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());

        return order;
    }
}
