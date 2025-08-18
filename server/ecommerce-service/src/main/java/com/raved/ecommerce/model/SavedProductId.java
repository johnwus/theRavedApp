package com.raved.ecommerce.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for SavedProduct entity
 */
@Embeddable
public class SavedProductId implements Serializable {
    
    private Long userId;
    private Long productId;
    
    // Constructors
    public SavedProductId() {
    }
    
    public SavedProductId(Long userId, Long productId) {
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
        SavedProductId that = (SavedProductId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
    
    @Override
    public String toString() {
        return "SavedProductId{" +
                "userId=" + userId +
                ", productId=" + productId +
                '}';
    }
}
