package com.fullstack.online_course_platform.model;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_courses_instructor_id", columnList = "instructor_id"),
                @Index(name = "idx_courses_status", columnList = "status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 250)
    private String slug;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Builder.Default
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 30)
    private CourseLevel level = CourseLevel.BEGINNER;

    @Builder.Default
    @Column(name = "language", nullable = false, length = 50)
    private String language = "Vietnamese";

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CourseStatus status = CourseStatus.DRAFT;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "course_categories",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_course_categories_course_category",
                    columnNames = {"course_id", "category_id"}
            )
    )
    private Set<Category> categories = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        @OrderBy("sortOrder ASC")
    private List<CourseRequirement> requirements = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        @OrderBy("sortOrder ASC")
    private List<CourseLearningOutcome> learningOutcomes = new ArrayList<>();

        public void addCategory(Category category) {
                categories.add(category);
        }

        public void removeCategory(Category category) {
                categories.remove(category);
        }

        public void addRequirement(CourseRequirement requirement) {
                requirements.add(requirement);
                requirement.setCourse(this);
        }

        public void removeRequirement(CourseRequirement requirement) {
                requirements.remove(requirement);
                requirement.setCourse(null);
        }

        public void addLearningOutcome(CourseLearningOutcome learningOutcome) {
                learningOutcomes.add(learningOutcome);
                learningOutcome.setCourse(this);
        }

        public void removeLearningOutcome(CourseLearningOutcome learningOutcome) {
                learningOutcomes.remove(learningOutcome);
                learningOutcome.setCourse(null);
        }
}