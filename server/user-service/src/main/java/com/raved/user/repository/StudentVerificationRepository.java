package com.raved.user.repository;

import com.raved.user.model.StudentVerification;
import com.raved.user.model.StudentVerification.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for StudentVerification entity operations
 */
@Repository
public interface StudentVerificationRepository extends JpaRepository<StudentVerification, Long> {

    /**
     * Find verification by user ID
     */
    Optional<StudentVerification> findByUserId(Long userId);

    /**
     * Find verification by user ID and university ID
     */
    Optional<StudentVerification> findByUserIdAndUniversityId(Long userId, Long universityId);

    /**
     * Find verifications by status
     */
    List<StudentVerification> findByStatus(VerificationStatus status);

    /**
     * Find verifications by status with pagination
     */
    Page<StudentVerification> findByStatus(VerificationStatus status, Pageable pageable);

    /**
     * Find verifications by university ID
     */
    List<StudentVerification> findByUniversityId(Long universityId);

    /**
     * Find verifications by university ID and status
     */
    List<StudentVerification> findByUniversityIdAndStatus(Long universityId, VerificationStatus status);

    /**
     * Find pending verifications older than specified date
     */
    @Query("SELECT sv FROM StudentVerification sv WHERE sv.status = 'PENDING' AND sv.submittedAt < :date")
    List<StudentVerification> findPendingVerificationsOlderThan(@Param("date") LocalDateTime date);

    /**
     * Count verifications by status
     */
    long countByStatus(VerificationStatus status);

    /**
     * Count verifications by university and status
     */
    long countByUniversityIdAndStatus(Long universityId, VerificationStatus status);

    /**
     * Check if student ID exists for a university
     */
    boolean existsByUniversityIdAndStudentId(Long universityId, String studentId);

    /**
     * Find verifications submitted today
     */
    @Query("SELECT COUNT(sv) FROM StudentVerification sv WHERE DATE(sv.submittedAt) = CURRENT_DATE")
    long countVerificationsSubmittedToday();
}
