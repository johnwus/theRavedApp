package com.raved.ecommerce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for ProductCategory entity
 */
public class ProductCategoryResponse {

    private Long id;
    private String name;
    private String description;
    private String slug;
    private String imageUrl;
    private Boolean isActive;
    private Integer displayOrder;
    private Long parentCategoryId;
    private String parentCategoryName;
    
    // Metrics
    private Integer productCount;
    private Integer activeProductCount;
    
    // Subcategories
    private List<ProductCategoryResponse> subcategories;
    
    // SEO fields
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ProductCategoryResponse() {
    }

    public ProductCategoryResponse(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getActiveProductCount() {
        return activeProductCount;
    }

    public void setActiveProductCount(Integer activeProductCount) {
        this.activeProductCount = activeProductCount;
    }

    public List<ProductCategoryResponse> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ProductCategoryResponse> subcategories) {
        this.subcategories = subcategories;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
