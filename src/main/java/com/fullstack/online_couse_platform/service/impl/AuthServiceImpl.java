package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.enums.InstructorStatus;
import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.common.enums.UserStatus;
import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RefreshTokenRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.TokenResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.mapper.UserMapper;
import com.fullstack.online_couse_platform.model.Instructor;
import com.fullstack.online_couse_platform.model.Learner;
import com.fullstack.online_couse_platform.model.Role;
import com.fullstack.online_couse_platform.model.User;
import com.fullstack.online_couse_platform.repository.InstructorRepository;
import com.fullstack.online_couse_platform.repository.LearnerRepository;
import com.fullstack.online_couse_platform.repository.RoleRepository;
import com.fullstack.online_couse_platform.repository.UserRepository;
import com.fullstack.online_couse_platform.service.AuthService;
import com.fullstack.online_couse_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public UserResponse registerLearner(RegisterLearnerRequest request) {
        User savedUser = createBaseUser(request.email(), request.password(), RoleType.LEARNER);

        Learner learner = Learner.builder()
                .user(savedUser)
                .fullName(request.fullName())
                .avatarUrl(request.avatarUrl())
                .bio(request.bio())
                .build();
        learnerRepository.save(learner);

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse registerInstructor(RegisterInstructorRequest request) {
        User savedUser = createBaseUser(request.email(), request.password(), RoleType.INSTRUCTOR);

        Instructor instructor = Instructor.builder()
                .user(savedUser)
                .fullName(request.fullName())
                .avatarUrl(request.avatarUrl())
                .bio(request.bio())
                .expertise(request.expertise())
                .experienceYears(request.experienceYears())
                .status(InstructorStatus.PENDING)
                .build();
        instructorRepository.save(instructor);

        return userMapper.toUserResponse(savedUser);
    }

    private User createBaseUser(String email, String password, RoleType roleType) {
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(roleType.name())
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(roleType.name())
                                .description(roleType.name() + " role")
                                .build()
                ));

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authentication);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return tokenService.createTokenResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        var userId = tokenService.getRefreshTokenUserId(request.refreshToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return tokenService.createTokenResponse(user);
    }
}
