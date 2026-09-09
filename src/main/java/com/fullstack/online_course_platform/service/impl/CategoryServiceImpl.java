package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.utils.SlugUtils;
import com.fullstack.online_course_platform.dto.request.CreateCategoryRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCategoryRequest;
import com.fullstack.online_course_platform.dto.response.CategoryResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.CategoryMapper;
import com.fullstack.online_course_platform.model.Category;
import com.fullstack.online_course_platform.repository.CategoryRepository;
import com.fullstack.online_course_platform.repository.CourseRepository;
import com.fullstack.online_course_platform.service.CategoryService;
import com.fullstack.online_course_platform.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CATEGORY-SERVICE")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryTree() {
        return categoryRepository.findAllByParentIsNullOrderByNameAsc().stream()
            .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getBySlug(String slug) {
        return categoryMapper.toResponse(categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    @Override
    public PageResponse<CourseSummaryResponse> getPublishedCourses(String slug, Pageable pageable) {
        if (!categoryRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return courseService.findPublished(null, slug, null, null, null, null, pageable);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        String slug = request.slug() == null || request.slug().isBlank()
                ? SlugUtils.slugify(request.name()) : request.slug();
        ensureSlugAvailable(slug, null);
        Category category = Category.builder()
                .name(request.name().trim())
                .slug(slug)
                .description(request.description())
                .parent(resolveParent(request.parentId()))
                .build();
        Category saved = categoryRepository.save(category);
        log.info("Category created: categoryId={}", saved.getId());
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        Category category = findCategory(categoryId);
        if (request.name() != null) {
            category.setName(request.name().trim());
        }
        if (request.slug() != null) {
            ensureSlugAvailable(request.slug(), categoryId);
            category.setSlug(request.slug());
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }
        if (request.clearParent()) {
            category.setParent(null);
        } else if (request.parentId() != null) {
            Category parent = resolveParent(request.parentId());
            ensureValidParent(category, parent);
            category.setParent(parent);
        }
        categoryRepository.save(category);
        log.info("Category updated: categoryId={}", categoryId);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = findCategory(categoryId);
        if (!category.getChildren().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
        }
        if (courseRepository.existsByCategoriesId(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_IN_USE);
        }
        categoryRepository.delete(category);
        log.info("Category deleted: categoryId={}", categoryId);
    }

    private Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Category resolveParent(UUID parentId) {
        return parentId == null ? null : findCategory(parentId);
    }

    private void ensureSlugAvailable(String slug, UUID currentId) {
        categoryRepository.findBySlug(slug).ifPresent(existing -> {
            if (!existing.getId().equals(currentId)) {
                throw new AppException(ErrorCode.CATEGORY_SLUG_ALREADY_EXISTS);
            }
        });
    }

    private void ensureValidParent(Category category, Category parent) {
        Category current = parent;
        while (current != null) {
            if (current.getId().equals(category.getId())) {
                throw new AppException(ErrorCode.BAD_REQUEST, "Category hierarchy cannot contain a cycle");
            }
            current = current.getParent();
        }
    }

}