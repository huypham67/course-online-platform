package com.fullstack.online_course_platform.mapper;

import com.fullstack.online_course_platform.dto.response.CategoryResponse;
import com.fullstack.online_course_platform.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "parentId", source = "parent.id", qualifiedByName = "uuidToString")
    CategoryResponse toResponse(Category category);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }
}
