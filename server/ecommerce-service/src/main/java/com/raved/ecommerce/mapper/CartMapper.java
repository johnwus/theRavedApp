package com.raved.ecommerce.mapper;

import com.raved.ecommerce.dto.response.CartResponse;
import com.raved.ecommerce.model.Cart;
import org.springframework.stereotype.Component;

/**
 * Mapper for Cart entity and DTOs
 */
@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        // Note: totalAmount, itemCount, discountCode, discountAmount not supported in
        // current model
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        return response;
    }
}