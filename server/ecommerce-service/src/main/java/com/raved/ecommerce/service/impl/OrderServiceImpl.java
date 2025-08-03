package com.raved.ecommerce.service.impl;

import com.raved.ecommerce.dto.request.CreateOrderRequest;
import com.raved.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.raved.ecommerce.dto.response.OrderResponse;
import com.raved.ecommerce.exception.OrderNotFoundException;
import com.raved.ecommerce.exception.InvalidOrderStatusException;
import com.raved.ecommerce.mapper.OrderMapper;
import com.raved.ecommerce.model.Order;
import com.raved.ecommerce.repository.OrderRepository;
import com.raved.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        logger.info("Creating new order for buyer: {}", request.getBuyerId());
        
        Order order = orderMapper.toOrder(request);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponse> getOrderById(Long id) {
        logger.debug("Getting order by ID: {}", id);
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        return orderOpt.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByBuyer(Long buyerId, Pageable pageable) {
        logger.debug("Getting orders for buyer ID: {}", buyerId);
        
        Page<Order> orders = orderRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId, pageable);
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersBySeller(Long sellerId, Pageable pageable) {
        logger.debug("Getting orders for seller ID: {}", sellerId);
        
        Page<Order> orders = orderRepository.findBySellerIdOrderByCreatedAtDesc(sellerId, pageable);
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        logger.info("Updating order status for ID: {} to {}", orderId, request.getStatus());
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus());
        
        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new InvalidOrderStatusException("Invalid status transition from " + 
                    order.getStatus() + " to " + newStatus);
        }
        
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        // Set specific timestamps based on status
        switch (newStatus) {
            case CONFIRMED:
                order.setConfirmedAt(LocalDateTime.now());
                break;
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                order.setCancellationReason(request.getReason());
                break;
        }
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Order status updated successfully for ID: {}", orderId);
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, String reason) {
        logger.info("Cancelling order with ID: {}", orderId);
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        
        // Check if order can be cancelled
        if (order.getStatus() == Order.OrderStatus.DELIVERED || 
            order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(reason);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Order cancelled successfully with ID: {}", orderId);
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, Pageable pageable) {
        logger.debug("Getting orders by status: {}", status);
        
        Page<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Getting orders by date range: {} to {}", startDate, endDate);
        
        Page<Order> orders = orderRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate, pageable);
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(Long orderId) {
        logger.debug("Calculating total for order ID: {}", orderId);
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        return order.getTotalAmount();
    }

    @Override
    public OrderResponse processOrderPayment(Long orderId, String paymentMethodId) {
        logger.info("Processing payment for order ID: {} with method: {}", orderId, paymentMethodId);
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        
        // TODO: Integrate with actual payment processor
        // For now, we'll simulate payment processing
        order.setPaymentMethodId(paymentMethodId);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Payment processed successfully for order ID: {}", orderId);
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics(Long sellerId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting order statistics for seller ID: {} from {} to {}", sellerId, startDate, endDate);
        
        long totalOrders = orderRepository.countBySellerIdAndCreatedAtBetween(sellerId, startDate, endDate);
        BigDecimal totalRevenue = orderRepository.sumTotalAmountBySellerIdAndCreatedAtBetween(sellerId, startDate, endDate);
        long pendingOrders = orderRepository.countBySellerIdAndStatusAndCreatedAtBetween(
                sellerId, Order.OrderStatus.PENDING, startDate, endDate);
        long completedOrders = orderRepository.countBySellerIdAndStatusAndCreatedAtBetween(
                sellerId, Order.OrderStatus.DELIVERED, startDate, endDate);
        long cancelledOrders = orderRepository.countBySellerIdAndStatusAndCreatedAtBetween(
                sellerId, Order.OrderStatus.CANCELLED, startDate, endDate);
        
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }
        
        return new OrderStatistics(totalOrders, totalRevenue, pendingOrders, completedOrders, cancelledOrders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRecentOrders(int limit) {
        logger.debug("Getting recent orders with limit: {}", limit);
        
        List<Order> orders = orderRepository.findRecentOrders(limit);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse confirmDelivery(Long orderId) {
        logger.info("Confirming delivery for order ID: {}", orderId);
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Delivery confirmed successfully for order ID: {}", orderId);
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse requestRefund(Long orderId, String reason) {
        logger.info("Requesting refund for order ID: {}", orderId);
        
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        
        // Check if refund can be requested
        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Refund can only be requested for delivered orders");
        }
        
        order.setStatus(Order.OrderStatus.REFUND_REQUESTED);
        order.setRefundReason(reason);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Refund requested successfully for order ID: {}", orderId);
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean isValidStatusTransition(Order.OrderStatus currentStatus, Order.OrderStatus newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case PENDING:
                return newStatus == Order.OrderStatus.CONFIRMED || newStatus == Order.OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == Order.OrderStatus.SHIPPED || newStatus == Order.OrderStatus.CANCELLED;
            case SHIPPED:
                return newStatus == Order.OrderStatus.DELIVERED;
            case DELIVERED:
                return newStatus == Order.OrderStatus.REFUND_REQUESTED;
            case CANCELLED:
            case REFUND_REQUESTED:
            case REFUNDED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
