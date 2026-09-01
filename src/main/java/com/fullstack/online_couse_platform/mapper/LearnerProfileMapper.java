package com.fullstack.online_couse_platform.mapper;

import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.dto.response.LearnerProfileResponse;
import com.fullstack.online_couse_platform.model.Learner;
import com.fullstack.online_couse_platform.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface LearnerProfileMapper {

    @Mapping(target = "id", source = "learner.user.id", qualifiedByName = "uuidToString")
    @Mapping(target = "email", source = "learner.user.email")
    @Mapping(target = "role", expression = "java(mapRoleType(learner.getUser()))")
    @Mapping(target = "status", source = "learner.user.status")
    @Mapping(target = "fullName", source = "learner.fullName")
    @Mapping(target = "avatarUrl", source = "learner.avatarUrl")
    @Mapping(target = "bio", source = "learner.bio")
    @Mapping(target = "createdAt", source = "learner.user.createdAt", qualifiedByName = "instantToString")
    @Mapping(target = "updatedAt", source = "learner.user.updatedAt", qualifiedByName = "instantToString")
    LearnerProfileResponse toLearnerProfileResponse(Learner learner);

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
            return com.fullstack.online_couse_platform.common.enums.RoleType.valueOf(user.getRole().getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
