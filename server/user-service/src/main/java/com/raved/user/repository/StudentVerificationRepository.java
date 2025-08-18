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
    Optional<StudentVerification> findByUser_Id(Long userId);

    /**
     * Find verification by user ID and status
     */
    Optional<StudentVerification> findByUser_IdAndVerificationStatus(Long userId, VerificationStatus verificationStatus);

    /**
     * Find verification by user ID and university ID
     */
    Optional<StudentVerification> findByUser_IdAndUniversity_Id(Long userId, Long universityId);

    /**
     * Find verifications by status
     */
    List<StudentVerification> findByVerificationStatus(VerificationStatus verificationStatus);

    /**
     * Find verifications by status with pagination
     */
    Page<StudentVerification> findByVerificationStatus(VerificationStatus verificationStatus, Pageable pageable);

    /**
     * Find verifications by status ordered by submitted date ascending
     */
    Page<StudentVerification> findByVerificationStatusOrderBySubmittedAtAsc(VerificationStatus verificationStatus, Pageable pageable);

    /**
     * Find verifications by status ordered by submitted date descending
     */
    Page<StudentVerification> findByVerificationStatusOrderBySubmittedAtDesc(VerificationStatus verificationStatus, Pageable pageable);

    /**
     * Find verifications by university ID
     */
    List<StudentVerification> findByUniversity_Id(Long universityId);

    /**
     * Find verifications by university ID and status
     */
    List<StudentVerification> findByUniversity_IdAndVerificationStatus(Long universityId, VerificationStatus verificationStatus);

    /**
     * Find pending verifications older than specified date
     */
    @Query("SELECT sv FROM StudentVerification sv WHERE sv.verificationStatus = 'PENDING' AND sv.submittedAt < :date")
    List<StudentVerification> findPendingVerificationsOlderThan(@Param("date") LocalDateTime date);

    /**
     * Count verifications by status
     */
    long countByVerificationStatus(VerificationStatus verificationStatus);

    /**
     * Count verifications by university and status
     */
    long countByUniversity_IdAndVerificationStatus(Long universityId, VerificationStatus verificationStatus);

    /**
     * Check if student ID exists for a university
     */
    boolean existsByUniversity_IdAndUser_StudentId(Long universityId, String studentId);

    /**
     * Find verifications submitted today
     */
    @Query("SELECT COUNT(sv) FROM StudentVerification sv WHERE DATE(sv.submittedAt) = CURRENT_DATE")
    long countVerificationsSubmittedToday();
}
