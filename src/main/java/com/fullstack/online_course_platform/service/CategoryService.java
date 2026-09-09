package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.CreateCategoryRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCategoryRequest;
import com.fullstack.online_course_platform.dto.response.CategoryResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryResponse> getCategoryTree();
    CategoryResponse getBySlug(String slug);
    PageResponse<CourseSummaryResponse> getPublishedCourses(String slug, Pageable pageable);
    CategoryResponse createCategory(CreateCategoryRequest request);
    void updateCategory(UUID categoryId, UpdateCategoryRequest request);
    void deleteCategory(UUID categoryId);
}