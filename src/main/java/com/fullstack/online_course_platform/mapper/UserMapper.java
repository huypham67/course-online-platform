package com.fullstack.online_course_platform.mapper;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "role", expression = "java(mapRoleType(user))")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "instantToString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "instantToString")
    UserResponse toUserResponse(User user);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
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
