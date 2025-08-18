package com.raved.ecommerce.controller;

import com.raved.ecommerce.dto.request.CreateOrderRequest;
import com.raved.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.raved.ecommerce.dto.response.OrderResponse;
import com.raved.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Order Management
 * Handles order lifecycle from creation to fulfillment
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Management", description = "APIs for managing orders and order lifecycle")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create order from cart")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication authentication) {

        logger.info("Creating new order for user: {}", authentication.getName());

        try {
            OrderResponse order = orderService.createOrder(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "order", order,
                "message", "Order created successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to create order: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get user's order history")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getUserOrders(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int pageSize,
            Authentication authentication) {

        logger.debug("Getting orders for user: {}", authentication.getName());

        // TODO: Extract user ID from authentication
        Long userId = 1L; // Placeholder - should extract from JWT token

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<OrderResponse> orders = orderService.getOrdersByBuyer(userId, pageable);

        Map<String, Object> response = Map.of(
            "success", true,
            "orders", orders.getContent(),
            "pagination", Map.of(
                "page", orders.getNumber(),
                "size", orders.getSize(),
                "totalElements", orders.getTotalElements(),
                "totalPages", orders.getTotalPages(),
                "hasNext", orders.hasNext()
            )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get specific order details")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        logger.debug("Getting order by ID: {} for user: {}", orderId, authentication.getName());

        Optional<OrderResponse> orderOpt = orderService.getOrderById(orderId);

        if (orderOpt.isPresent()) {
            // TODO: Add authorization check - user should only see their own orders or orders for their products

            Map<String, Object> response = Map.of(
                "success", true,
                "order", orderOpt.get()
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Order not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update order status")
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            Authentication authentication) {

        logger.info("Updating order status for order ID: {} by user: {}", orderId, authentication.getName());

        try {
            // TODO: Add authorization check - only seller or admin should be able to update order status

            OrderResponse order = orderService.updateOrderStatus(orderId, request);

            Map<String, Object> response = Map.of(
                "success", true,
                "order", order,
                "message", "Order status updated successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating order status for order {}: {}", orderId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update order status: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Cancel order")
    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable Long orderId,
            @Parameter(description = "Cancellation reason") @RequestParam(required = false) String reason,
            Authentication authentication) {

        logger.info("Cancelling order ID: {} by user: {}", orderId, authentication.getName());

        try {
            // TODO: Add authorization check - only buyer should be able to cancel their order

            OrderResponse order = orderService.cancelOrder(orderId, reason);

            Map<String, Object> response = Map.of(
                "success", true,
                "order", order,
                "message", "Order cancelled successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error cancelling order {}: {}", orderId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to cancel order: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Confirm order delivery")
    @PostMapping("/{orderId}/confirm-delivery")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> confirmDelivery(
            @PathVariable Long orderId,
            Authentication authentication) {

        logger.info("Confirming delivery for order ID: {} by user: {}", orderId, authentication.getName());

        try {
            // TODO: Add authorization check - only buyer should be able to confirm delivery

            OrderResponse order = orderService.confirmDelivery(orderId);

            Map<String, Object> response = Map.of(
                "success", true,
                "order", order,
                "message", "Delivery confirmed successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error confirming delivery for order {}: {}", orderId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to confirm delivery: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Request order refund")
    @PostMapping("/{orderId}/refund")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> requestRefund(
            @PathVariable Long orderId,
            @Parameter(description = "Refund reason") @RequestParam String reason,
            Authentication authentication) {

        logger.info("Requesting refund for order ID: {} by user: {}", orderId, authentication.getName());

        try {
            // TODO: Add authorization check - only buyer should be able to request refund

            OrderResponse order = orderService.requestRefund(orderId, reason);

            Map<String, Object> response = Map.of(
                "success", true,
                "order", order,
                "message", "Refund request submitted successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error requesting refund for order {}: {}", orderId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to request refund: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get recent orders (admin only)")
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getRecentOrders(
            @Parameter(description = "Number of orders to return") @RequestParam(defaultValue = "10") int limit) {

        logger.debug("Getting recent orders with limit: {}", limit);

        List<OrderResponse> orders = orderService.getRecentOrders(limit);

        Map<String, Object> response = Map.of(
            "success", true,
            "orders", orders
        );

        return ResponseEntity.ok(response);
    }
}
