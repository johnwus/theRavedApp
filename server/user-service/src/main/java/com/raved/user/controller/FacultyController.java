package com.raved.user.controller;

import com.raved.user.dto.response.FacultyResponse;
import com.raved.user.model.Faculty;
import com.raved.user.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for faculty operations
 */
@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    private FacultyResponse toResponse(Faculty f) {
        FacultyResponse r = new FacultyResponse();
        r.setId(f.getId());
        if (f.getUniversity() != null) {
            r.setUniversityId(f.getUniversity().getId());
            r.setUniversityName(f.getUniversity().getName());
        }
        r.setName(f.getName());
        r.setCode(f.getCode());
        r.setDescription(f.getDescription());
        r.setIsActive(Boolean.TRUE.equals(f.getIsActive()));
        return r;
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<FacultyResponse>> getByUniversity(@PathVariable Long universityId) {
        List<FacultyResponse> list = facultyRepository.findByUniversity_Id(universityId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FacultyResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacultyResponse> result = facultyRepository.searchFaculty(q, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(result);
    }
}
