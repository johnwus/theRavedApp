package com.raved.ecommerce.service;

import com.raved.ecommerce.dto.request.CreateOrderRequest;
import com.raved.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.raved.ecommerce.dto.response.OrderResponse;
import com.raved.ecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * OrderService for TheRavedApp
 */
public interface OrderService {

    /**
     * Create a new order
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * Get order by ID
     */
    Optional<OrderResponse> getOrderById(Long id);

    /**
     * Get orders by buyer
     */
    Page<OrderResponse> getOrdersByBuyer(Long buyerId, Pageable pageable);

    /**
     * Get orders by seller
     */
    Page<OrderResponse> getOrdersBySeller(Long sellerId, Pageable pageable);

    /**
     * Update order status
     */
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

    /**
     * Cancel order
     */
    OrderResponse cancelOrder(Long orderId, String reason);

    /**
     * Get orders by status
     */
    Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, Pageable pageable);

    /**
     * Get orders by date range
     */
    Page<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Calculate order total
     */
    BigDecimal calculateOrderTotal(Long orderId);

    /**
     * Process order payment
     */
    OrderResponse processOrderPayment(Long orderId, String paymentMethodId);

    /**
     * Get order statistics for seller
     */
    OrderStatistics getOrderStatistics(Long sellerId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get recent orders
     */
    List<OrderResponse> getRecentOrders(int limit);

    /**
     * Confirm order delivery
     */
    OrderResponse confirmDelivery(Long orderId);

    /**
     * Request order refund
     */
    OrderResponse requestRefund(Long orderId, String reason);

    /**
     * Inner class for order statistics
     */
    class OrderStatistics {
        private long totalOrders;
        private BigDecimal totalRevenue;
        private long pendingOrders;
        private long completedOrders;
        private long cancelledOrders;

        // Constructors, getters, and setters
        public OrderStatistics() {}

        public OrderStatistics(long totalOrders, BigDecimal totalRevenue, long pendingOrders,
                             long completedOrders, long cancelledOrders) {
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.pendingOrders = pendingOrders;
            this.completedOrders = completedOrders;
            this.cancelledOrders = cancelledOrders;
        }

        // Getters and setters
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }

        public long getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(long completedOrders) { this.completedOrders = completedOrders; }

        public long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
    }
}
