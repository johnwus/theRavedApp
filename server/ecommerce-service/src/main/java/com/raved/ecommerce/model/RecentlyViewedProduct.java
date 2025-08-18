package com.raved.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * RecentlyViewedProduct entity for TheRavedApp
 * Represents products recently viewed by users.
 * Based on the recently_viewed_products table schema from migration V7.
 */
@Entity
@Table(name = "recently_viewed_products")
public class RecentlyViewedProduct {
    
    @EmbeddedId
    private RecentlyViewedProductId id;
    
    @Column(name = "viewed_at", nullable = false, updatable = false)
    private LocalDateTime viewedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    
    // Constructors
    public RecentlyViewedProduct() {
    }
    
    public RecentlyViewedProduct(Long userId, Long productId) {
        this.id = new RecentlyViewedProductId(userId, productId);
        this.viewedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public RecentlyViewedProductId getId() {
        return id;
    }
    
    public void setId(RecentlyViewedProductId id) {
        this.id = id;
    }
    
    public LocalDateTime getViewedAt() {
        return viewedAt;
    }
    
    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Long getUserId() {
        return id != null ? id.getUserId() : null;
    }
    
    public Long getProductId() {
        return id != null ? id.getProductId() : null;
    }
    
    @Override
    public String toString() {
        return "RecentlyViewedProduct{" +
                "userId=" + getUserId() +
                ", productId=" + getProductId() +
                ", viewedAt=" + viewedAt +
                '}';
    }
}
