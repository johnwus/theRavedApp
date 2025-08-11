package com.raved.ecommerce.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for RecentlyViewedProduct entity
 */
@Embeddable
public class RecentlyViewedProductId implements Serializable {
    
    private Long userId;
    private Long productId;
    
    // Constructors
    public RecentlyViewedProductId() {
    }
    
    public RecentlyViewedProductId(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
    
    // Getters and setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentlyViewedProductId that = (RecentlyViewedProductId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
    
    @Override
    public String toString() {
        return "RecentlyViewedProductId{" +
                "userId=" + userId +
                ", productId=" + productId +
                '}';
    }
}
