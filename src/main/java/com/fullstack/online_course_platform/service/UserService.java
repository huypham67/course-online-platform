package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(String email, String password, RoleType roleType);
}
