package com.raved.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * SavedProduct entity for TheRavedApp
 * Represents products saved by users for later viewing.
 * Based on the saved_products table schema from migration V6.
 */
@Entity
@Table(name = "saved_products")
public class SavedProduct {
    
    @EmbeddedId
    private SavedProductId id;
    
    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    
    // Constructors
    public SavedProduct() {
    }
    
    public SavedProduct(Long userId, Long productId) {
        this.id = new SavedProductId(userId, productId);
        this.savedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public SavedProductId getId() {
        return id;
    }
    
    public void setId(SavedProductId id) {
        this.id = id;
    }
    
    public LocalDateTime getSavedAt() {
        return savedAt;
    }
    
    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
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
        return "SavedProduct{" +
                "userId=" + getUserId() +
                ", productId=" + getProductId() +
                ", savedAt=" + savedAt +
                '}';
    }
}
