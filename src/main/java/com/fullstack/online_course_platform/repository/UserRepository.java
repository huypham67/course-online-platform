package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("select user from User user join fetch user.role role " +
            "where (:keyword is null or lower(user.email) like lower(concat('%', :keyword, '%')) " +
            "or lower(user.fullName) like lower(concat('%', :keyword, '%'))) " +
            "and (:role is null or role.name = :role) " +
            "and (:status is null or user.status = :status)")
    Page<User> search(@Param("keyword") String keyword,
                      @Param("role") String role,
                      @Param("status") com.fullstack.online_course_platform.common.enums.UserStatus status,
                      Pageable pageable);
}
