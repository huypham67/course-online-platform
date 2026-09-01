package com.fullstack.online_couse_platform.mapper;

import com.fullstack.online_couse_platform.dto.response.InstructorProfileResponse;
import com.fullstack.online_couse_platform.model.Instructor;
import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface InstructorProfileMapper {

    @Mapping(target = "id", source = "instructor.user.id", qualifiedByName = "uuidToString")
    @Mapping(target = "email", source = "instructor.user.email")
    @Mapping(target = "role", expression = "java(mapRoleType(instructor.getUser()))")
    @Mapping(target = "status", source = "instructor.user.status")
    @Mapping(target = "fullName", source = "instructor.fullName")
    @Mapping(target = "avatarUrl", source = "instructor.avatarUrl")
    @Mapping(target = "bio", source = "instructor.bio")
    @Mapping(target = "expertise", source = "instructor.expertise")
    @Mapping(target = "experienceYears", source = "instructor.experienceYears")
    @Mapping(target = "instructorStatus", source = "instructor.status")
    @Mapping(target = "createdAt", source = "instructor.user.createdAt", qualifiedByName = "instantToString")
    @Mapping(target = "updatedAt", source = "instructor.user.updatedAt", qualifiedByName = "instantToString")
    InstructorProfileResponse toInstructorProfileResponse(Instructor instructor);

    @Named("uuidToString")
    default String uuidToString(java.util.UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    default RoleType mapRoleType(User user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return null;
        }
        try {
            return RoleType.valueOf(user.getRole().getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
