package com.raved.user.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * University Entity for TheRavedApp
 *
 * Represents universities in the system for student verification.
 * This entity stores university information including location,
 * domain suffixes, and active status.
 */
@Entity
@Table(name = "universities", indexes = {
        @Index(name = "idx_universities_code", columnList = "code"),
        @Index(name = "idx_universities_name", columnList = "name"),
        @Index(name = "idx_universities_country", columnList = "country")
})
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // University identification
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    // Location information
    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    // Additional information
    @Column(name = "domain_suffix", length = 50)
    private String domainSuffix;

    @Column(name = "logo_url", columnDefinition = "text")
    private String logoUrl;

    // Status and timestamps
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    // Default constructor
    public University() {}

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

    public String getCode() { 
        return code; 
    }
    
    public void setCode(String code) { 
        this.code = code; 
    }

    public String getCountry() { 
        return country; 
    }
    
    public void setCountry(String country) { 
        this.country = country; 
    }

    public String getCity() { 
        return city; 
    }
    
    public void setCity(String city) { 
        this.city = city; 
    }

    public String getDomainSuffix() { 
        return domainSuffix; 
    }
    
    public void setDomainSuffix(String domainSuffix) { 
        this.domainSuffix = domainSuffix; 
    }

    public String getLogoUrl() { 
        return logoUrl; 
    }
    
    public void setLogoUrl(String logoUrl) { 
        this.logoUrl = logoUrl; 
    }

    public Boolean getIsActive() { 
        return isActive; 
    }
    
    public void setIsActive(Boolean isActive) { 
        this.isActive = isActive; 
    }

    public Instant getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(Instant createdAt) { 
        this.createdAt = createdAt; 
    }

    public Instant getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(Instant updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "University{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
