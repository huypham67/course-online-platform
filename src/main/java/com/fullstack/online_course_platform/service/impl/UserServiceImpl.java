package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.dto.response.UserStatusResponse;
import com.fullstack.online_course_platform.dto.request.UpdateCurrentUserRequest;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.UserMapper;
import com.fullstack.online_course_platform.model.Role;
import com.fullstack.online_course_platform.model.User;
import com.fullstack.online_course_platform.repository.RoleRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.UserService;
import com.fullstack.online_course_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    @Transactional
    public UserResponse createUser(String email, String password, String fullName, RoleType roleType) {
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(roleType.name())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(roleType.name())
                        .description(roleType.name() + " role")
                        .build()));

        User user = User.builder()
                .email(email)
                .fullName(fullName)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findUsers(String keyword, RoleType role, UserStatus status, Pageable pageable) {
        String roleName = role == null ? null : role.name();
        return PageResponse.from(userRepository.search(keyword, roleName, status, pageable).map(userMapper::toUserResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public UserStatusResponse updateStatus(UUID userId, UserStatus status) {
        if (userId.equals(SecurityUtils.getCurrentUserId()) && status == UserStatus.INACTIVE) {
            throw new AppException(ErrorCode.FORBIDDEN, "Administrators cannot deactivate their own account");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(status);
        User saved = userRepository.save(user);
        if (status == UserStatus.INACTIVE) {
            tokenService.revokeActiveTokens(userId);
        }
        return UserStatusResponse.builder()
                .id(saved.getId().toString())
                .status(saved.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        return getUser(SecurityUtils.getCurrentUserId());
    }

    @Override
    @Transactional
    public void updateCurrentUser(UpdateCurrentUserRequest request) {
        User user = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(request, user);
        userRepository.save(user);
    }
}
