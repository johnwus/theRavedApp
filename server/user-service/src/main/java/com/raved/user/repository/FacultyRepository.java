package com.raved.user.repository;

import com.raved.user.model.Faculty;
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
     * Search faculty by name or code
     */
    @Query("SELECT f FROM Faculty f WHERE f.name LIKE %:searchTerm% OR f.code LIKE %:searchTerm%")
    Page<Faculty> searchFaculty(@Param("searchTerm") String searchTerm, Pageable pageable);







    /**
     * Count faculty by university
     */
    long countByUniversityId(Long universityId);





    /**
     * Check if faculty ID exists for a university
     */
    boolean existsByUniversityIdAndFacultyId(Long universityId, String facultyId);


}
