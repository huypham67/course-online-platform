package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.enums.InstructorStatus;
import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.common.enums.UserStatus;
import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.LoginResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;
    private final UserMapper userMapper;

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
                .passwordHash(password)
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!user.getPasswordHash().equals(request.password())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        return LoginResponse.builder()
                .accessToken("mock-access-token-" + UUID.randomUUID())
                .refreshToken("mock-refresh-token-" + UUID.randomUUID())
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }
}
