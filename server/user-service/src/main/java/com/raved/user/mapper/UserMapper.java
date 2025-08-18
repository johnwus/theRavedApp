package com.raved.user.mapper;

import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.dto.response.ProfileResponse;
import com.raved.user.model.User;
import com.raved.user.dto.request.UpdateProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * MapStruct mapper for User entity and DTOs
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Convert User entity to UserResponse DTO
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserResponse toUserResponse(User user);

    /**
     * Convert User entity to ProfileResponse DTO
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "student", ignore = true)
    ProfileResponse toProfileResponse(User user);

    /**
     * Convert RegisterRequest DTO to User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "universityId", ignore = true)
    @Mapping(target = "facultyId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    User toUser(RegisterRequest request);

    /**
     * Update User entity from RegisterRequest DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "universityId", ignore = true)
    @Mapping(target = "facultyId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    void updateUserFromRequest(RegisterRequest request, @MappingTarget User user);

    void updateUserFromUpdateProfileRequest(UpdateProfileRequest request, @MappingTarget User user);

    /**
     * Convert Instant to LocalDateTime
     */
    default LocalDateTime map(Instant instant) {
        return instant != null ? instant.atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }
}
        