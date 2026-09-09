package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.dto.request.CourseCategoriesRequest;
import com.fullstack.online_course_platform.dto.request.CourseItemsRequest;
import com.fullstack.online_course_platform.dto.request.CreateCourseRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCourseRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCourseThumbnailRequest;
import com.fullstack.online_course_platform.dto.response.CourseCreatedResponse;
import com.fullstack.online_course_platform.dto.response.CourseDetailResponse;
import com.fullstack.online_course_platform.dto.response.CourseStatusResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.CourseThumbnailResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface CourseService {

    PageResponse<CourseSummaryResponse> findPublished(String keyword, String category, UUID instructorId, CourseLevel level,
                                                       String language, BigDecimal minPrice, BigDecimal maxPrice,
                                                       Pageable pageable);
    CourseDetailResponse getPublishedBySlug(String slug);
    PageResponse<CourseSummaryResponse> findInstructorCourses(String keyword, CourseStatus status, Pageable pageable);
    CourseDetailResponse getInstructorCourse(UUID courseId);
    CourseCreatedResponse createCourse(CreateCourseRequest request);
    void updateCourse(UUID courseId, UpdateCourseRequest request);
    void deleteDraft(UUID courseId);
    void replaceCategories(UUID courseId, CourseCategoriesRequest request);
    void replaceRequirements(UUID courseId, CourseItemsRequest request);
    void replaceLearningOutcomes(UUID courseId, CourseItemsRequest request);
    CourseThumbnailResponse updateThumbnail(UUID courseId, UpdateCourseThumbnailRequest request);
    CourseStatusResponse submitForReview(UUID courseId);
    CourseStatusResponse pauseOwnCourse(UUID courseId);
    CourseStatusResponse resumeOwnCourse(UUID courseId);
    CourseStatusResponse archiveOwnCourse(UUID courseId);
        PageResponse<CourseSummaryResponse> findAdminCourses(
            String keyword, CourseStatus status, UUID instructorId, Pageable pageable);
    CourseDetailResponse getAdminCourse(UUID courseId);
    CourseStatusResponse approveCourse(UUID courseId);
    CourseStatusResponse rejectCourse(UUID courseId, String reason);
    CourseStatusResponse pauseCourse(UUID courseId);
    CourseStatusResponse resumeCourse(UUID courseId);
    CourseStatusResponse archiveCourse(UUID courseId);
}