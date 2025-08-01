package com.raved.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Faculty Entity for TheRavedApp
 *
 * Represents academic faculties within universities (e.g., Faculty of
 * Engineering).
 * Based on the faculties table schema.
 */
@Entity
@Table(name = "faculties", indexes = {
        @Index(name = "idx_faculties_university", columnList = "university_id"),
        @Index(name = "idx_faculties_code", columnList = "code")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_faculty_university_code", columnNames = { "university_id", "code" })
})
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @NotBlank(message = "Faculty name is required")
    @Size(max = 255, message = "Faculty name must not exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Faculty code is required")
    @Size(max = 20, message = "Faculty code must not exceed 20 characters")
    @Column(nullable = false)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 7, message = "Color code must not exceed 7 characters")
    @Column(name = "color_code")
    private String colorCode; // hex color for UI theming

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> departments;

    // Constructors
    public Faculty() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Faculty(University university, String name, String code) {
        this();
        this.university = university;
        this.name = name;
        this.code = code;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", university=" + (university != null ? university.getName() : null) +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }

    // Enums
    public enum FacultyStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
