package com.raved.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for creating a new faculty
 */
public class FacultyCreateRequest {

    @NotNull(message = "University ID is required")
    private Long universityId;

    @NotBlank(message = "Faculty name is required")
    @Size(max = 255, message = "Faculty name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Faculty code is required")
    @Size(max = 10, message = "Faculty code must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Faculty code must contain only uppercase letters and numbers")
    private String code;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 255, message = "Dean name must not exceed 255 characters")
    private String deanName;

    @Size(max = 255, message = "Dean email must not exceed 255 characters")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Dean email should be valid")
    private String deanEmail;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email should be valid")
    private String email;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Size(max = 500, message = "Location must not exceed 500 characters")
    private String location;

    private String imageUrl;

    // Constructors
    public FacultyCreateRequest() {}

    // Getters and Setters
    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
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

    public String getDeanName() {
        return deanName;
    }

    public void setDeanName(String deanName) {
        this.deanName = deanName;
    }

    public String getDeanEmail() {
        return deanEmail;
    }

    public void setDeanEmail(String deanEmail) {
        this.deanEmail = deanEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
