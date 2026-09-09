package com.fullstack.online_course_platform.repository;

import com.fullstack.online_course_platform.model.PasswordResetToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select token from PasswordResetToken token " +
            "join fetch token.user " +
            "where token.tokenHash = :tokenHash and token.usedAt is null and token.expiresAt > :now")
    Optional<PasswordResetToken> findActiveTokenForUpdate(
            @Param("tokenHash") String tokenHash,
            @Param("now") Instant now);

    @Modifying
    @Query("update PasswordResetToken token set token.usedAt = :usedAt " +
            "where token.user.id = :userId and token.usedAt is null")
    void invalidateActiveTokensByUserId(@Param("userId") UUID userId, @Param("usedAt") Instant usedAt);
}