package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {

    boolean existsBySlug(String slug);

    boolean existsByCategoriesId(UUID categoryId);

    Optional<Course> findBySlugAndStatus(String slug, CourseStatus status);

    Optional<Course> findByIdAndInstructorUserId(UUID id, UUID userId);

    Optional<Course> findWithDetailsById(UUID id);
}