package com.raved.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user profile response containing comprehensive user information
 * including personal details, academic affiliations, and verification status.
 */
public class ProfileResponse {

    // Basic user information
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String profilePictureUrl;
    private String bio;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    // Account status and timestamps
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Academic affiliations
    private UniversityInfo university;
    private FacultyInfo faculty;
    private StudentInfo student;

    // Default constructor
    public ProfileResponse() {}

    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
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

    public String getFullName() { 
        return fullName; 
    }
    
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }

    public String getProfilePictureUrl() { 
        return profilePictureUrl; 
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) { 
        this.profilePictureUrl = profilePictureUrl; 
    }

    public String getBio() { 
        return bio; 
    }
    
    public void setBio(String bio) { 
        this.bio = bio; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public LocalDate getDateOfBirth() { 
        return dateOfBirth; 
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        this.dateOfBirth = dateOfBirth; 
    }

    public Boolean getEmailVerified() { 
        return emailVerified; 
    }
    
    public void setEmailVerified(Boolean emailVerified) { 
        this.emailVerified = emailVerified; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getLastLogin() { 
        return lastLogin; 
    }
    
    public void setLastLogin(LocalDateTime lastLogin) { 
        this.lastLogin = lastLogin; 
    }

    public UniversityInfo getUniversity() { 
        return university; 
    }
    
    public void setUniversity(UniversityInfo university) { 
        this.university = university; 
    }

    public FacultyInfo getFaculty() { 
        return faculty; 
    }
    
    public void setFaculty(FacultyInfo faculty) { 
        this.faculty = faculty; 
    }

    public StudentInfo getStudent() { 
        return student; 
    }
    
    public void setStudent(StudentInfo student) { 
        this.student = student; 
    }

    /**
     * Nested class representing university information
     */
    public static class UniversityInfo {
        private Long id;
        private String name;
        private String code;
        private String logoUrl;

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

        public String getLogoUrl() { 
            return logoUrl; 
        }
        
        public void setLogoUrl(String logoUrl) { 
            this.logoUrl = logoUrl; 
        }
    }

    /**
     * Nested class representing faculty member information
     */
    public static class FacultyInfo {
        private String facultyId;
        private String position;
        private String officeLocation;

        // Getters and Setters
        public String getFacultyId() { 
            return facultyId; 
        }
        
        public void setFacultyId(String facultyId) { 
            this.facultyId = facultyId; 
        }

        public String getPosition() { 
            return position; 
        }
        
        public void setPosition(String position) { 
            this.position = position; 
        }

        public String getOfficeLocation() { 
            return officeLocation; 
        }
        
        public void setOfficeLocation(String officeLocation) { 
            this.officeLocation = officeLocation; 
        }
    }

    /**
     * Nested class representing student information
     */
    public static class StudentInfo {
        private String studentId;
        private String verificationStatus;
        private LocalDateTime verifiedAt;

        // Getters and Setters
        public String getStudentId() { 
            return studentId; 
        }
        
        public void setStudentId(String studentId) { 
            this.studentId = studentId; 
        }

        public String getVerificationStatus() { 
            return verificationStatus; 
        }
        
        public void setVerificationStatus(String verificationStatus) { 
            this.verificationStatus = verificationStatus; 
        }

        public LocalDateTime getVerifiedAt() { 
            return verifiedAt; 
        }
        
        public void setVerifiedAt(LocalDateTime verifiedAt) { 
            this.verifiedAt = verifiedAt; 
        }
    }
}
