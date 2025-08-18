package com.raved.ecommerce.controller;

import com.raved.ecommerce.service.ProductService;
import com.raved.ecommerce.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Inventory Management
 * Handles inventory tracking and stock management operations
 */
@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory Management", description = "APIs for managing product inventory and stock levels")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get low stock products for seller")
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getLowStockProducts(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "10") int threshold,
            Authentication authentication) {

        logger.debug("Getting low stock products for user: {} with threshold: {}", authentication.getName(), threshold);

        try {
            // TODO: Extract seller ID from authentication
            Long sellerId = 1L; // Placeholder - should extract from JWT token

            List<ProductResponse> products = productService.getLowStockProducts(sellerId, threshold);

            Map<String, Object> response = Map.of(
                "success", true,
                "products", products,
                "threshold", threshold,
                "count", products.size()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting low stock products for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get low stock products: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Update product inventory")
    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateProductInventory(
            @PathVariable Long productId,
            @Parameter(description = "New quantity") @RequestParam int quantity,
            Authentication authentication) {

        logger.info("Updating inventory for product {} to quantity {} by user: {}", productId, quantity, authentication.getName());

        try {
            // TODO: Add authorization check - only product owner should be able to update inventory

            productService.updateProductInventory(productId, quantity);

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Product inventory updated successfully",
                "productId", productId,
                "newQuantity", quantity
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating inventory for product {} by user {}: {}", productId, authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update product inventory: " + e.getMessage()
            );
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @Operation(summary = "Get inventory alerts (admin only)")
    @GetMapping("/alerts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getInventoryAlerts() {

        logger.debug("Getting inventory alerts");

        try {
            // TODO: Implement comprehensive inventory alerts
            // For now, return low stock products across all sellers

            Map<String, Object> response = Map.of(
                "success", true,
                "alerts", Map.of(
                    "lowStock", "Low stock alerts would be implemented here",
                    "outOfStock", "Out of stock alerts would be implemented here",
                    "overStock", "Over stock alerts would be implemented here"
                ),
                "message", "Inventory alerts feature coming soon"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting inventory alerts: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get inventory alerts: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
