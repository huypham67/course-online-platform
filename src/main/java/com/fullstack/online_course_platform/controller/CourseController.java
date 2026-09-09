package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.CourseDetailResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Public course catalog APIs")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Browse published courses")
    public ApiResult<PageResponse<CourseSummaryResponse>> getPublishedCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID instructorId,
            @RequestParam(required = false) CourseLevel level,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20, sort = "publishedAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Published courses retrieved successfully",
                courseService.findPublished(keyword, category, instructorId, level, language, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get published course details")
    public ApiResult<CourseDetailResponse> getPublishedCourse(@PathVariable String slug) {
        return ApiResult.of(HttpStatus.OK, "Course retrieved successfully",
                courseService.getPublishedBySlug(slug));
    }
}