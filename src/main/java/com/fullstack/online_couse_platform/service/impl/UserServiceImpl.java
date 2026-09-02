package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.common.enums.UserStatus;
import com.fullstack.online_couse_platform.dto.response.UserResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.mapper.UserMapper;
import com.fullstack.online_couse_platform.model.Role;
import com.fullstack.online_couse_platform.model.User;
import com.fullstack.online_couse_platform.repository.RoleRepository;
import com.fullstack.online_couse_platform.repository.UserRepository;
import com.fullstack.online_couse_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(String email, String password, RoleType roleType) {
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
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
