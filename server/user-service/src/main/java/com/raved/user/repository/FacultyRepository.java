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
     * Find faculties by university ID (via relationship)
     */
    List<Faculty> findByUniversity_Id(Long universityId);

    /**
     * Search faculty by name or code
     */
    @Query("SELECT f FROM Faculty f WHERE f.name LIKE %:searchTerm% OR f.code LIKE %:searchTerm%")
    Page<Faculty> searchFaculty(@Param("searchTerm") String searchTerm, Pageable pageable);







    /**
     * Count faculties by university ID
     */
    long countByUniversity_Id(Long universityId);





    /**
     * Check if a faculty code exists for a university
     */
    boolean existsByUniversity_IdAndCode(Long universityId, String code);


}
