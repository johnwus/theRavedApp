package com.raved.user.mapper;

import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.dto.response.ProfileResponse;
import com.raved.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for User entity and DTOs
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
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
    @Mapping(target = "university", source = "university")
    @Mapping(target = "faculty", source = "faculty")
    @Mapping(target = "student", source = "studentVerification")
    ProfileResponse toProfileResponse(User user);

    /**
     * Convert RegisterRequest DTO to User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "studentVerification", ignore = true)
    User toUser(RegisterRequest request);

    /**
     * Update User entity from RegisterRequest DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "studentVerification", ignore = true)
    void updateUserFromRequest(RegisterRequest request, @MappingTarget User user);
}
