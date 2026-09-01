package com.fullstack.online_couse_platform.repository;

import com.fullstack.online_couse_platform.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    Optional<Instructor> findByUserId(UUID userId);
}
