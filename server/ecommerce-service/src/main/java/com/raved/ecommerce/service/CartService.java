package com.raved.ecommerce.service;

import com.raved.ecommerce.dto.request.AddToCartRequest;
import com.raved.ecommerce.dto.request.UpdateCartItemRequest;
import com.raved.ecommerce.dto.response.CartResponse;
import com.raved.ecommerce.dto.response.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * CartService for TheRavedApp
 */
public interface CartService {

    /**
     * Add item to cart
     */
    CartItemResponse addToCart(AddToCartRequest request);

    /**
     * Update cart item quantity
     */
    CartItemResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request);

    /**
     * Remove item from cart
     */
    void removeFromCart(Long cartItemId);

    /**
     * Get user's cart
     */
    CartResponse getUserCart(Long userId);

    /**
     * Clear user's cart
     */
    void clearCart(Long userId);

    /**
     * Get cart total
     */
    BigDecimal getCartTotal(Long userId);

    /**
     * Get cart item count
     */
    int getCartItemCount(Long userId);

    /**
     * Check if product is in cart
     */
    boolean isProductInCart(Long userId, Long productId);

    /**
     * Move cart item to wishlist
     */
    void moveToWishlist(Long cartItemId);

    /**
     * Apply discount code
     */
    CartResponse applyDiscountCode(Long userId, String discountCode);

    /**
     * Remove discount code
     */
    CartResponse removeDiscountCode(Long userId);

    /**
     * Validate cart before checkout
     */
    CartValidationResult validateCart(Long userId);

    /**
     * Inner class for cart validation result
     */
    class CartValidationResult {
        private boolean isValid;
        private List<String> errors;
        private List<String> warnings;

        public CartValidationResult() {}

        public CartValidationResult(boolean isValid, List<String> errors, List<String> warnings) {
            this.isValid = isValid;
            this.errors = errors;
            this.warnings = warnings;
        }

        // Getters and setters
        public boolean isValid() { return isValid; }
        public void setValid(boolean valid) { isValid = valid; }

        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }

        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    }
}
