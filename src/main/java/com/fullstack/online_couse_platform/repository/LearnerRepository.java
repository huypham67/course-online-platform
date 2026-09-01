package com.fullstack.online_couse_platform.repository;

import com.fullstack.online_couse_platform.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LearnerRepository extends JpaRepository<Learner, UUID> {
    Optional<Learner> findByUserId(UUID userId);
}
