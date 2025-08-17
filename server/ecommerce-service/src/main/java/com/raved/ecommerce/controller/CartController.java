package com.raved.ecommerce.controller;

import com.raved.ecommerce.dto.request.AddToCartRequest;
import com.raved.ecommerce.dto.request.UpdateCartItemRequest;
import com.raved.ecommerce.dto.response.CartResponse;
import com.raved.ecommerce.dto.response.CartItemResponse;
import com.raved.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Shopping Cart Management
 * Handles cart operations including add, update, remove, and checkout preparation
 */
@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Shopping Cart", description = "APIs for managing shopping cart operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Operation(summary = "Get user's shopping cart")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getUserCart(Authentication authentication) {
        
        logger.debug("Getting cart for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            Long userId = 1L; // Placeholder - should extract from JWT token

            CartResponse cart = cartService.getUserCart(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cart", cart
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting cart for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to get cart: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Add item to cart")
    @PostMapping("/items")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {
        
        logger.info("Adding item to cart for user: {} - Product ID: {}", authentication.getName(), request.getProductId());

        try {
            CartItemResponse cartItem = cartService.addToCart(request);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cartItem", cartItem,
                "message", "Item added to cart successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error adding item to cart for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to add item to cart: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {
        
        logger.info("Updating cart item {} for user: {}", itemId, authentication.getName());

        try {
            CartItemResponse cartItem = cartService.updateCartItem(itemId, request);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cartItem", cartItem,
                "message", "Cart item updated successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating cart item {} for user {}: {}", itemId, authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update cart item: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Long itemId,
            Authentication authentication) {
        
        logger.info("Removing cart item {} for user: {}", itemId, authentication.getName());

        try {
            cartService.removeFromCart(itemId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Item removed from cart successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error removing cart item {} for user {}: {}", itemId, authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to remove item from cart: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Clear entire cart")
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> clearCart(Authentication authentication) {
        
        logger.info("Clearing cart for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            Long userId = 1L; // Placeholder - should extract from JWT token

            cartService.clearCart(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Cart cleared successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error clearing cart for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to clear cart: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Validate cart before checkout")
    @PostMapping("/validate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> validateCart(Authentication authentication) {
        
        logger.debug("Validating cart for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            Long userId = 1L; // Placeholder - should extract from JWT token

            CartService.CartValidationResult validation = cartService.validateCart(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "validation", Map.of(
                    "isValid", validation.isValid(),
                    "errors", validation.getErrors(),
                    "warnings", validation.getWarnings()
                )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error validating cart for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to validate cart: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Apply discount code to cart")
    @PostMapping("/discount")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> applyDiscountCode(
            @Parameter(description = "Discount code") @RequestParam String discountCode,
            Authentication authentication) {
        
        logger.info("Applying discount code {} for user: {}", discountCode, authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            Long userId = 1L; // Placeholder - should extract from JWT token

            CartResponse cart = cartService.applyDiscountCode(userId, discountCode);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cart", cart,
                "message", "Discount code applied successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error applying discount code {} for user {}: {}", discountCode, authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to apply discount code: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Remove discount code from cart")
    @DeleteMapping("/discount")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> removeDiscountCode(Authentication authentication) {
        
        logger.info("Removing discount code for user: {}", authentication.getName());

        try {
            // TODO: Extract user ID from authentication
            Long userId = 1L; // Placeholder - should extract from JWT token

            CartResponse cart = cartService.removeDiscountCode(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cart", cart,
                "message", "Discount code removed successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error removing discount code for user {}: {}", authentication.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to remove discount code: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
