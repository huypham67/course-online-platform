package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.model.Instructor;
import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    Optional<Instructor> findByUserId(UUID userId);
        Optional<Instructor> findByIdAndStatus(UUID id, InstructorStatus status);

    @Query("select instructor from Instructor instructor join fetch instructor.user user " +
            "where (:keyword is null or lower(user.email) like lower(concat('%', :keyword, '%')) " +
            "or lower(user.fullName) like lower(concat('%', :keyword, '%'))) " +
            "and (:status is null or instructor.status = :status)")
    Page<Instructor> search(@Param("keyword") String keyword,
                            @Param("status") InstructorStatus status,
                            Pageable pageable);
}
