package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = "children")
    List<Category> findAllByParentIsNullOrderByNameAsc();

    @EntityGraph(attributePaths = "children")
    Optional<Category> findBySlug(String slug);

    Set<Category> findAllByIdIn(Set<UUID> ids);
}