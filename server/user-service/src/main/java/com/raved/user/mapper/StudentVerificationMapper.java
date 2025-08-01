package com.raved.user.mapper;

import com.raved.user.dto.request.VerificationRequest;
import com.raved.user.dto.response.VerificationResponse;
import com.raved.user.model.StudentVerification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for StudentVerification entity and DTOs
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentVerificationMapper {

    /**
     * Convert StudentVerification entity to VerificationResponse DTO
     */
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "universityName", source = "university.name")
    VerificationResponse toVerificationResponse(StudentVerification verification);

    /**
     * Convert VerificationRequest DTO to StudentVerification entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    StudentVerification toStudentVerification(VerificationRequest request);
}
