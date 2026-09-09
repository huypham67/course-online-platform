package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.CategoryResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Public course category APIs")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get category tree")
    public ApiResult<List<CategoryResponse>> getCategories() {
        return ApiResult.of(HttpStatus.OK, "Categories retrieved successfully", categoryService.getCategoryTree());
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get category details")
    public ApiResult<CategoryResponse> getCategory(@PathVariable String slug) {
        return ApiResult.of(HttpStatus.OK, "Category retrieved successfully", categoryService.getBySlug(slug));
    }

    @GetMapping("/{slug}/courses")
    @Operation(summary = "Get published courses in category")
    public ApiResult<PageResponse<CourseSummaryResponse>> getCategoryCourses(
            @PathVariable String slug,
            @PageableDefault(size = 20, sort = "publishedAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Category courses retrieved successfully",
                categoryService.getPublishedCourses(slug, pageable));
    }
}