package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.UserStatus;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.dto.request.LoginRequest;
import com.fullstack.online_course_platform.dto.request.RefreshTokenRequest;
import com.fullstack.online_course_platform.dto.response.TokenResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.model.User;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.AuthService;
import com.fullstack.online_course_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-SERVICE")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authentication);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("User login succeeded: userId={}, email={}", user.getId(), user.getEmail());
        return tokenService.createTokenResponse(user);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        User user = tokenService.consumeRefreshToken(request.refreshToken());

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        log.info("Refresh token rotation succeeded: userId={}", user.getId());
        return tokenService.createTokenResponse(user);
    }

    @Override
    @Transactional
    public void logout() {
        var userId = SecurityUtils.getCurrentUserId();
        tokenService.revokeActiveTokens(userId);
        log.info("User logged out: userId={}", userId);
    }
}
