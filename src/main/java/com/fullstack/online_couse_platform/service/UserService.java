package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(String email, String password, RoleType roleType);
}
