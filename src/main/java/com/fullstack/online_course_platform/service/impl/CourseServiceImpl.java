package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.common.utils.SlugUtils;
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
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.CourseResponseMapper;
import com.fullstack.online_course_platform.model.Course;
import com.fullstack.online_course_platform.model.CourseLearningOutcome;
import com.fullstack.online_course_platform.model.CourseRequirement;
import com.fullstack.online_course_platform.model.Instructor;
import com.fullstack.online_course_platform.repository.CategoryRepository;
import com.fullstack.online_course_platform.repository.CourseRepository;
import com.fullstack.online_course_platform.repository.CourseSpecifications;
import com.fullstack.online_course_platform.repository.InstructorRepository;
import com.fullstack.online_course_platform.service.CourseService;
import com.fullstack.online_course_platform.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-SERVICE")
public class CourseServiceImpl implements CourseService {

    private static final Set<CourseStatus> EDITABLE_STATUSES = Set.of(CourseStatus.DRAFT, CourseStatus.REJECTED);

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;
    private final CourseResponseMapper courseResponseMapper;
    private final StorageService storageService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CourseSummaryResponse> findPublished(
            String keyword, String category, UUID instructorId, CourseLevel level, String language,
            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        var specification = CourseSpecifications.filter(
                CourseStatus.PUBLISHED, null, keyword, category, level, language, minPrice, maxPrice);
        if (instructorId != null) {
            specification = specification.and(CourseSpecifications.byInstructorId(instructorId));
        }
        return PageResponse.from(courseRepository.findAll(specification, pageable).map(courseResponseMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getPublishedBySlug(String slug) {
        Course course = courseRepository.findBySlugAndStatus(slug, CourseStatus.PUBLISHED)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseResponseMapper.toDetail(course);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CourseSummaryResponse> findInstructorCourses(
            String keyword, CourseStatus status, Pageable pageable) {
        var specification = CourseSpecifications.filter(
                status, SecurityUtils.getCurrentUserId(), keyword, null, null, null, null, null);
        return PageResponse.from(courseRepository.findAll(specification, pageable).map(courseResponseMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getInstructorCourse(UUID courseId) {
        return courseResponseMapper.toDetail(findOwnedCourse(courseId));
    }

    @Override
    @Transactional
    public CourseCreatedResponse createCourse(CreateCourseRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        if (instructor.getStatus() != InstructorStatus.APPROVED) {
            throw new AppException(ErrorCode.FORBIDDEN, "Only approved instructors can create courses");
        }

        Course course = Course.builder()
                .instructor(instructor)
                .title(request.title().trim())
                .slug(uniqueSlug(request.title()))
                .shortDescription(request.shortDescription())
                .description(request.description())
                .price(request.price())
                .level(request.level())
                .language(request.language().trim())
                .build();
        Course saved = courseRepository.save(course);
        log.info("Course created: courseId={}, instructorId={}", saved.getId(), instructor.getId());
        return CourseCreatedResponse.builder()
            .id(saved.getId().toString())
            .slug(saved.getSlug())
            .status(saved.getStatus())
            .build();
    }

    @Override
    @Transactional
    public void updateCourse(UUID courseId, UpdateCourseRequest request) {
        Course course = editableOwnedCourse(courseId);
        if (request.title() != null) {
            course.setTitle(request.title().trim());
        }
        if (request.shortDescription() != null) {
            course.setShortDescription(request.shortDescription());
        }
        if (request.description() != null) {
            course.setDescription(request.description());
        }
        if (request.price() != null) {
            course.setPrice(request.price());
        }
        if (request.level() != null) {
            course.setLevel(request.level());
        }
        if (request.language() != null) {
            course.setLanguage(request.language().trim());
        }
        courseRepository.save(course);
        log.info("Course updated: courseId={}", courseId);
    }

    @Override
    @Transactional
    public void deleteDraft(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        requireStatus(course, CourseStatus.DRAFT);
        courseRepository.delete(course);
        log.info("Draft course deleted: courseId={}", courseId);
    }

    @Override
    @Transactional
    public void replaceCategories(UUID courseId, CourseCategoriesRequest request) {
        Course course = editableOwnedCourse(courseId);
        var categories = categoryRepository.findAllByIdIn(request.categoryIds());
        if (categories.size() != request.categoryIds().size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        course.getCategories().clear();
        course.getCategories().addAll(categories);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void replaceRequirements(UUID courseId, CourseItemsRequest request) {
        Course course = editableOwnedCourse(courseId);
        course.getRequirements().clear();
        request.items().forEach(item -> course.addRequirement(CourseRequirement.builder()
                .content(item.content().trim()).sortOrder(item.sortOrder()).build()));
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void replaceLearningOutcomes(UUID courseId, CourseItemsRequest request) {
        Course course = editableOwnedCourse(courseId);
        course.getLearningOutcomes().clear();
        request.items().forEach(item -> course.addLearningOutcome(CourseLearningOutcome.builder()
                .content(item.content().trim()).sortOrder(item.sortOrder()).build()));
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public CourseThumbnailResponse updateThumbnail(UUID courseId, UpdateCourseThumbnailRequest request) {
        Course course = editableOwnedCourse(courseId);
        course.setThumbnailUrl(request.thumbnailUrl());
        courseRepository.save(course);
        return CourseThumbnailResponse.builder()
            .thumbnailUrl(storageService.generatePresignedGetUrl(course.getThumbnailUrl()))
            .build();
    }

    @Override
    @Transactional
    public CourseStatusResponse submitForReview(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        requireStatus(course, CourseStatus.DRAFT, CourseStatus.REJECTED);
        course.setStatus(CourseStatus.PENDING_REVIEW);
        course.setRejectionReason(null);
        return saveStatus(course, "submitted for review");
    }

    @Override
    @Transactional
    public CourseStatusResponse pauseOwnCourse(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        requireStatus(course, CourseStatus.PUBLISHED);
        course.setStatus(CourseStatus.PAUSED);
        return saveStatus(course, "paused");
    }

    @Override
    @Transactional
    public CourseStatusResponse resumeOwnCourse(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        requireStatus(course, CourseStatus.PAUSED);
        course.setStatus(CourseStatus.PUBLISHED);
        return saveStatus(course, "resumed");
    }

    @Override
    @Transactional
    public CourseStatusResponse archiveOwnCourse(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        requireStatus(course, CourseStatus.DRAFT, CourseStatus.REJECTED, CourseStatus.PUBLISHED, CourseStatus.PAUSED);
        course.setStatus(CourseStatus.ARCHIVED);
        return saveStatus(course, "archived");
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CourseSummaryResponse> findAdminCourses(
            String keyword, CourseStatus status, UUID instructorId, Pageable pageable) {
        var specification = CourseSpecifications.filter(status, null, keyword, null, null, null, null, null);
        if (instructorId != null) {
            specification = specification.and(CourseSpecifications.byInstructorId(instructorId));
        }
        return PageResponse.from(courseRepository.findAll(specification, pageable).map(courseResponseMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getAdminCourse(UUID courseId) {
        Course course = courseRepository.findWithDetailsById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseResponseMapper.toDetail(course);
    }

    @Override
    @Transactional
    public CourseStatusResponse approveCourse(UUID courseId) {
        Course course = findCourse(courseId);
        requireStatus(course, CourseStatus.PENDING_REVIEW);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setRejectionReason(null);
        if (course.getPublishedAt() == null) {
            course.setPublishedAt(Instant.now());
        }
        return saveStatus(course, "approved");
    }

    @Override
    @Transactional
    public CourseStatusResponse rejectCourse(UUID courseId, String reason) {
        Course course = findCourse(courseId);
        requireStatus(course, CourseStatus.PENDING_REVIEW);
        course.setStatus(CourseStatus.REJECTED);
        course.setRejectionReason(reason.trim());
        return saveStatus(course, "rejected");
    }

    @Override
    @Transactional
    public CourseStatusResponse pauseCourse(UUID courseId) {
        Course course = findCourse(courseId);
        requireStatus(course, CourseStatus.PUBLISHED);
        course.setStatus(CourseStatus.PAUSED);
        return saveStatus(course, "paused by admin");
    }

    @Override
    @Transactional
    public CourseStatusResponse resumeCourse(UUID courseId) {
        Course course = findCourse(courseId);
        requireStatus(course, CourseStatus.PAUSED);
        course.setStatus(CourseStatus.PUBLISHED);
        return saveStatus(course, "resumed by admin");
    }

    @Override
    @Transactional
    public CourseStatusResponse archiveCourse(UUID courseId) {
        Course course = findCourse(courseId);
        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new AppException(ErrorCode.COURSE_STATUS_CONFLICT);
        }
        course.setStatus(CourseStatus.ARCHIVED);
        return saveStatus(course, "archived by admin");
    }

    private Course editableOwnedCourse(UUID courseId) {
        Course course = findOwnedCourse(courseId);
        if (!EDITABLE_STATUSES.contains(course.getStatus())) {
            throw new AppException(ErrorCode.COURSE_STATUS_CONFLICT);
        }
        return course;
    }

    private Course findOwnedCourse(UUID courseId) {
        return courseRepository.findByIdAndInstructorUserId(courseId, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    private Course findCourse(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    private void requireStatus(Course course, CourseStatus... allowedStatuses) {
        if (Arrays.stream(allowedStatuses).noneMatch(status -> status == course.getStatus())) {
            throw new AppException(ErrorCode.COURSE_STATUS_CONFLICT);
        }
    }

    private CourseStatusResponse saveStatus(Course course, String action) {
        Course saved = courseRepository.save(course);
        log.info("Course {}: courseId={}, status={}", action, saved.getId(), saved.getStatus());
        return CourseStatusResponse.builder()
            .id(saved.getId().toString())
            .status(saved.getStatus())
            .rejectionReason(saved.getRejectionReason())
            .publishedAt(saved.getPublishedAt())
            .build();
    }

    private String uniqueSlug(String title) {
        String base = SlugUtils.slugify(title);
        if (!courseRepository.existsBySlug(base)) {
            return base;
        }
        return base + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}