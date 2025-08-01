package com.raved.user.repository;

import com.raved.user.model.Faculty;
import com.raved.user.model.Faculty.FacultyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Faculty entity operations
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    /**
     * Find faculty by user ID
     */
    Optional<Faculty> findByUserId(Long userId);

    /**
     * Find faculty by university ID
     */
    List<Faculty> findByUniversityId(Long universityId);

    /**
     * Find faculty by university ID and status
     */
    List<Faculty> findByUniversityIdAndStatus(Long universityId, FacultyStatus status);

    /**
     * Find faculty by department
     */
    List<Faculty> findByDepartment(String department);

    /**
     * Find faculty by university ID and department
     */
    List<Faculty> findByUniversityIdAndDepartment(Long universityId, String department);

    /**
     * Find faculty by status
     */
    List<Faculty> findByStatus(FacultyStatus status);

    /**
     * Find faculty by status with pagination
     */
    Page<Faculty> findByStatus(FacultyStatus status, Pageable pageable);

    /**
     * Search faculty by name or department
     */
    @Query("SELECT f FROM Faculty f JOIN f.user u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.department) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.position) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Faculty> searchFaculty(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find active faculty members
     */
    @Query("SELECT f FROM Faculty f WHERE f.status = 'ACTIVE'")
    List<Faculty> findActiveFaculty();

    /**
     * Count faculty by university
     */
    long countByUniversityId(Long universityId);

    /**
     * Count faculty by university and status
     */
    long countByUniversityIdAndStatus(Long universityId, FacultyStatus status);

    /**
     * Count faculty by department
     */
    long countByDepartment(String department);

    /**
     * Check if faculty ID exists for a university
     */
    boolean existsByUniversityIdAndFacultyId(Long universityId, String facultyId);

    /**
     * Find faculty by university and position
     */
    List<Faculty> findByUniversityIdAndPosition(Long universityId, String position);
}
