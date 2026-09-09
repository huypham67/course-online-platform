package com.fullstack.online_course_platform.mapper;

import com.fullstack.online_course_platform.dto.response.CourseDetailResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.model.Category;
import com.fullstack.online_course_platform.model.Course;
import com.fullstack.online_course_platform.model.CourseLearningOutcome;
import com.fullstack.online_course_platform.model.CourseRequirement;
import com.fullstack.online_course_platform.model.Instructor;
import com.fullstack.online_course_platform.service.StorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class CourseResponseMapper {

    @Autowired
    protected StorageService storageService;

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "thumbnailUrl", source = "thumbnailUrl", qualifiedByName = "resolveThumbnail")
    @Mapping(target = "instructorName", source = "instructor.user.fullName")
    public abstract CourseSummaryResponse toSummary(Course course);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "thumbnailUrl", source = "thumbnailUrl", qualifiedByName = "resolveThumbnail")
    public abstract CourseDetailResponse toDetail(Course course);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "fullName", source = "user.fullName")
    protected abstract CourseDetailResponse.InstructorInfo toInstructorInfo(Instructor instructor);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    protected abstract CourseDetailResponse.CategoryInfo toCategoryInfo(Category category);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    protected abstract CourseDetailResponse.CourseItem toCourseItem(CourseRequirement requirement);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    protected abstract CourseDetailResponse.CourseItem toCourseItem(CourseLearningOutcome learningOutcome);

    @Named("resolveThumbnail")
    protected String resolveThumbnail(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            return thumbnailUrl;
        }
        return storageService.generatePresignedGetUrl(thumbnailUrl);
    }

    @Named("uuidToString")
    protected String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }
}