package com.raved.ecommerce.service.impl;

import com.raved.ecommerce.dto.request.AddToCartRequest;
import com.raved.ecommerce.dto.request.UpdateCartItemRequest;
import com.raved.ecommerce.dto.response.CartResponse;
import com.raved.ecommerce.event.EcommerceEventPublisher;
import com.raved.ecommerce.exception.CartNotFoundException;
import com.raved.ecommerce.exception.InsufficientStockException;
import com.raved.ecommerce.exception.ProductNotFoundException;
import com.raved.ecommerce.mapper.CartMapper;
import com.raved.ecommerce.model.Cart;
import com.raved.ecommerce.model.CartItem;
import com.raved.ecommerce.model.Product;
import com.raved.ecommerce.repository.CartItemRepository;
import com.raved.ecommerce.repository.CartRepository;
import com.raved.ecommerce.repository.ProductRepository;
import com.raved.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of CartService
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private EcommerceEventPublisher eventPublisher;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        logger.info("Adding product {} to cart for user: {}", request.getProductId(), request.getUserId());
        
        // Validate product exists and has sufficient stock
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found: " + request.getProductId());
        }
        
        Product product = productOpt.get();
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + request.getProductId());
        }
        
        // Get or create cart
        Cart cart = getOrCreateCart(request.getUserId());
        
        // Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), request.getProductId());
        
        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            // Update existing item
            cartItem = existingItemOpt.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            if (product.getStockQuantity() < newQuantity) {
                throw new InsufficientStockException("Insufficient stock for total quantity: " + newQuantity);
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());
        }
        
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        
        // Update cart totals
        updateCartTotals(cart);
        
        // Publish event
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", request.getProductId());
        eventData.put("productName", product.getName());
        eventData.put("quantity", request.getQuantity());
        eventData.put("price", product.getPrice());
        
        eventPublisher.publishProductAddedToCartEvent(request.getUserId(), request.getProductId(), 
                product.getName(), request.getQuantity(), product.getPrice());
        
        logger.info("Product added to cart: {}", savedCartItem.getId());
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(UpdateCartItemRequest request) {
        logger.info("Updating cart item {} with quantity: {}", request.getCartItemId(), request.getQuantity());
        
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(request.getCartItemId());
        if (cartItemOpt.isEmpty()) {
            throw new CartNotFoundException("Cart item not found: " + request.getCartItemId());
        }
        
        CartItem cartItem = cartItemOpt.get();
        
        // Validate stock
        Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getStockQuantity() < request.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for quantity: " + request.getQuantity());
            }
        }
        
        cartItem.setQuantity(request.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
        
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        
        // Update cart totals
        updateCartTotals(cartItem.getCart());
        
        logger.info("Cart item updated: {}", savedCartItem.getId());
        return cartMapper.toCartResponse(cartItem.getCart());
    }

    @Override
    public CartResponse removeFromCart(Long cartItemId) {
        logger.info("Removing cart item: {}", cartItemId);
        
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        if (cartItemOpt.isEmpty()) {
            throw new CartNotFoundException("Cart item not found: " + cartItemId);
        }
        
        CartItem cartItem = cartItemOpt.get();
        Cart cart = cartItem.getCart();
        
        cartItemRepository.delete(cartItem);
        
        // Update cart totals
        updateCartTotals(cart);
        
        logger.info("Cart item removed: {}", cartItemId);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        logger.debug("Getting cart for user: {}", userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            // Return empty cart
            Cart emptyCart = new Cart();
            emptyCart.setUserId(userId);
            emptyCart.setTotalAmount(BigDecimal.ZERO);
            emptyCart.setItemCount(0);
            return cartMapper.toCartResponse(emptyCart);
        }
        
        return cartMapper.toCartResponse(cartOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public int getCartItemCount(Long userId) {
        logger.debug("Getting cart item count for user: {}", userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        return cartOpt.map(Cart::getItemCount).orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(Long userId) {
        logger.debug("Getting cart total for user: {}", userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        return cartOpt.map(Cart::getTotalAmount).orElse(BigDecimal.ZERO);
    }

    @Override
    public void clearCart(Long userId) {
        logger.info("Clearing cart for user: {}", userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cartItemRepository.deleteByCartId(cart.getId());
            
            cart.setTotalAmount(BigDecimal.ZERO);
            cart.setItemCount(0);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            
            logger.info("Cart cleared for user: {}", userId);
        }
    }

    @Override
    public void mergeGuestCart(Long userId, List<AddToCartRequest> guestCartItems) {
        logger.info("Merging guest cart with {} items for user: {}", guestCartItems.size(), userId);
        
        for (AddToCartRequest item : guestCartItems) {
            try {
                item.setUserId(userId);
                addToCart(item);
            } catch (Exception e) {
                logger.error("Error merging guest cart item: {}", item.getProductId(), e);
            }
        }
        
        logger.info("Guest cart merged for user: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInCart(Long userId, Long productId) {
        logger.debug("Checking if product {} is in cart for user: {}", productId, userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            return cartItemRepository.existsByCartIdAndProductId(cartOpt.get().getId(), productId);
        }
        return false;
    }

    @Override
    public void removeExpiredCarts(LocalDateTime cutoffDate) {
        logger.info("Removing expired carts older than: {}", cutoffDate);
        
        List<Cart> expiredCarts = cartRepository.findByUpdatedAtBefore(cutoffDate);
        for (Cart cart : expiredCarts) {
            cartItemRepository.deleteByCartId(cart.getId());
            cartRepository.delete(cart);
        }
        
        logger.info("Removed {} expired carts", expiredCarts.size());
    }

    @Override
    public CartResponse applyDiscount(Long userId, String discountCode) {
        logger.info("Applying discount code {} to cart for user: {}", discountCode, userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            throw new CartNotFoundException("Cart not found for user: " + userId);
        }
        
        Cart cart = cartOpt.get();
        
        // TODO: Implement discount logic
        // For now, just set the discount code
        cart.setDiscountCode(discountCode);
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart savedCart = cartRepository.save(cart);
        
        logger.info("Discount applied to cart for user: {}", userId);
        return cartMapper.toCartResponse(savedCart);
    }

    @Override
    public CartResponse removeDiscount(Long userId) {
        logger.info("Removing discount from cart for user: {}", userId);
        
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            throw new CartNotFoundException("Cart not found for user: " + userId);
        }
        
        Cart cart = cartOpt.get();
        cart.setDiscountCode(null);
        cart.setDiscountAmount(BigDecimal.ZERO);
        cart.setUpdatedAt(LocalDateTime.now());
        
        // Recalculate totals
        updateCartTotals(cart);
        
        logger.info("Discount removed from cart for user: {}", userId);
        return cartMapper.toCartResponse(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        
        // Create new cart
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setItemCount(0);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(cart);
    }

    private void updateCartTotals(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int itemCount = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        
        cart.setTotalAmount(totalAmount);
        cart.setItemCount(itemCount);
        cart.setUpdatedAt(LocalDateTime.now());
        
        cartRepository.save(cart);
    }
}
