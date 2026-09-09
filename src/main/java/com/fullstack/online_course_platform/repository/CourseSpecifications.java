package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.model.Category;
import com.fullstack.online_course_platform.model.Course;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public final class CourseSpecifications {

    private CourseSpecifications() {
    }

    public static Specification<Course> filter(
            CourseStatus status,
            UUID instructorUserId,
            String keyword,
            String categorySlug,
            CourseLevel level,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice) {
        return Specification.allOf(
                equalStatus(status),
                ownedBy(instructorUserId),
                keywordContains(keyword),
                inCategory(categorySlug),
                equalLevel(level),
                equalLanguage(language),
                priceAtLeast(minPrice),
                priceAtMost(maxPrice)
        );
    }

    private static Specification<Course> equalStatus(CourseStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private static Specification<Course> ownedBy(UUID userId) {
        return userId == null ? null : (root, query, cb) ->
                cb.equal(root.get("instructor").get("user").get("id"), userId);
    }

    private static Specification<Course> keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("shortDescription")), pattern)
        );
    }

    private static Specification<Course> inCategory(String categorySlug) {
        if (categorySlug == null || categorySlug.isBlank()) {
            return null;
        }
        return (root, query, cb) -> {
            query.distinct(true);
            var category = root.<Course, Category>join("categories", JoinType.INNER);
            return cb.equal(category.get("slug"), categorySlug);
        };
    }

    private static Specification<Course> equalLevel(CourseLevel level) {
        return level == null ? null : (root, query, cb) -> cb.equal(root.get("level"), level);
    }

    private static Specification<Course> equalLanguage(String language) {
        if (language == null || language.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.equal(cb.lower(root.get("language")), language.trim().toLowerCase());
    }

    private static Specification<Course> priceAtLeast(BigDecimal price) {
        return price == null ? null : (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), price);
    }

    private static Specification<Course> priceAtMost(BigDecimal price) {
        return price == null ? null : (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price);
    }
}