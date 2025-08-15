package com.raved.user.model;

import com.raved.common.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;

/**
 * User Entity for TheRavedApp
 * 
 * Represents a user in the system with authentication and profile information.
 * This entity stores comprehensive user data including personal details,
 * academic affiliations, and account preferences.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_student_id", columnList = "student_id")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student identification
    @Column(name = "student_id", nullable = true, unique = true, length = 50)
    private String studentId;

    // Authentication fields
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // Personal information
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 20)
    private String gender;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    // Academic affiliations
    @Column(name = "university_id")
    private Long universityId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "academic_year")
    private Integer academicYear;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    // Media and URLs
    @Column(name = "profile_picture_url", columnDefinition = "text")
    private String profilePictureUrl;

    @Column(name = "cover_photo_url", columnDefinition = "text")
    private String coverPhotoUrl;

    @Column(length = 255)
    private String location;

    @Column(name = "website_url", columnDefinition = "text")
    private String websiteUrl;

    // Privacy and visibility settings
    @Column(name = "is_profile_public")
    private Boolean isProfilePublic = true;

    @Column(name = "allow_messages_from_strangers")
    private Boolean allowMessagesFromStrangers = true;

    @Column(name = "show_email")
    private Boolean showEmail = false;

    @Column(name = "show_phone")
    private Boolean showPhone = false;

    // Verification status
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "student_id_verified")
    private Boolean studentIdVerified = false;

    @Column(name = "seller_verified")
    private Boolean sellerVerified = false;

    // Account status
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    // User role
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.STUDENT;

    // Timestamps
    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Default constructor
    public User() {}

    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getStudentId() { 
        return studentId; 
    }
    
    public void setStudentId(String studentId) { 
        this.studentId = studentId; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }

    // Alias for Spring Security compatibility
    public String getPassword() { 
        return passwordHash; 
    }

    public String getFirstName() { 
        return firstName; 
    }
    
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }
    
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public String getDisplayName() { 
        return displayName; 
    }
    
    public void setDisplayName(String displayName) { 
        this.displayName = displayName; 
    }

    public String getBio() { 
        return bio; 
    }
    
    public void setBio(String bio) { 
        this.bio = bio; 
    }

    public LocalDate getDateOfBirth() { 
        return dateOfBirth; 
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        this.dateOfBirth = dateOfBirth; 
    }

    public String getGender() { 
        return gender; 
    }
    
    public void setGender(String gender) { 
        this.gender = gender; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public Long getUniversityId() { 
        return universityId; 
    }
    
    public void setUniversityId(Long universityId) { 
        this.universityId = universityId; 
    }

    public Long getFacultyId() { 
        return facultyId; 
    }
    
    public void setFacultyId(Long facultyId) { 
        this.facultyId = facultyId; 
    }

    public Integer getAcademicYear() { 
        return academicYear; 
    }
    
    public void setAcademicYear(Integer academicYear) { 
        this.academicYear = academicYear; 
    }

    public Integer getGraduationYear() { 
        return graduationYear; 
    }
    
    public void setGraduationYear(Integer graduationYear) { 
        this.graduationYear = graduationYear; 
    }

    public String getProfilePictureUrl() { 
        return profilePictureUrl; 
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) { 
        this.profilePictureUrl = profilePictureUrl; 
    }

    public String getCoverPhotoUrl() { 
        return coverPhotoUrl; 
    }
    
    public void setCoverPhotoUrl(String coverPhotoUrl) { 
        this.coverPhotoUrl = coverPhotoUrl; 
    }

    public String getLocation() { 
        return location; 
    }
    
    public void setLocation(String location) { 
        this.location = location; 
    }

    public String getWebsiteUrl() { 
        return websiteUrl; 
    }
    
    public void setWebsiteUrl(String websiteUrl) { 
        this.websiteUrl = websiteUrl; 
    }

    public Boolean getIsProfilePublic() { 
        return isProfilePublic; 
    }
    
    public void setIsProfilePublic(Boolean isProfilePublic) { 
        this.isProfilePublic = isProfilePublic; 
    }

    public Boolean getAllowMessagesFromStrangers() { 
        return allowMessagesFromStrangers; 
    }
    
    public void setAllowMessagesFromStrangers(Boolean allowMessagesFromStrangers) { 
        this.allowMessagesFromStrangers = allowMessagesFromStrangers; 
    }

    public Boolean getShowEmail() { 
        return showEmail; 
    }
    
    public void setShowEmail(Boolean showEmail) { 
        this.showEmail = showEmail; 
    }

    public Boolean getShowPhone() { 
        return showPhone; 
    }
    
    public void setShowPhone(Boolean showPhone) { 
        this.showPhone = showPhone; 
    }

    public Boolean getEmailVerified() { 
        return emailVerified; 
    }
    
    public void setEmailVerified(Boolean emailVerified) { 
        this.emailVerified = emailVerified; 
    }

    public Boolean getStudentIdVerified() { 
        return studentIdVerified; 
    }
    
    public void setStudentIdVerified(Boolean studentIdVerified) { 
        this.studentIdVerified = studentIdVerified; 
    }

    public Boolean getSellerVerified() { 
        return sellerVerified; 
    }
    
    public void setSellerVerified(Boolean sellerVerified) { 
        this.sellerVerified = sellerVerified; 
    }

    public Boolean getIsActive() { 
        return isActive; 
    }
    
    public void setIsActive(Boolean isActive) { 
        this.isActive = isActive; 
    }

    public UserStatus getStatus() { 
        return status; 
    }
    
    public void setStatus(UserStatus status) { 
        this.status = status; 
    }

    public UserRole getRole() { 
        return role; 
    }
    
    public void setRole(UserRole role) { 
        this.role = role; 
    }

    // Returns roles as a collection for Spring Security compatibility
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getRoles() {
        return java.util.Collections.singletonList(
            new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    public Instant getLastLoginAt() { 
        return lastLoginAt; 
    }
    
    public void setLastLoginAt(Instant lastLoginAt) { 
        this.lastLoginAt = lastLoginAt; 
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

    // Utility methods
    /**
     * Returns the full name of the user
     * @return full name or username if names are not available
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }

    /**
     * Checks if the user is verified (email verified)
     * @return true if email is verified
     */
    public Boolean getIsVerified() {
        return emailVerified;
    }

    /**
     * Sets the verification status
     * @param isVerified verification status
     */
    public void setIsVerified(Boolean isVerified) {
        this.emailVerified = isVerified;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", emailVerified=" + emailVerified +
                ", createdAt=" + createdAt +
                '}';
    }
}
