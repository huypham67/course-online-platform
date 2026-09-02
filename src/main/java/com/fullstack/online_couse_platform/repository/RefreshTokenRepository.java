package com.fullstack.online_couse_platform.repository;

import com.fullstack.online_couse_platform.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByJtiAndRevokedAtIsNull(UUID jti);

    @Modifying
    @Query("update RefreshToken token set token.revokedAt = :revokedAt " +
            "where token.user.id = :userId and token.revokedAt is null")
    void revokeActiveTokensByUserId(@Param("userId") UUID userId, @Param("revokedAt") Instant revokedAt);
}
