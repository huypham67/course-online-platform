package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.response.AvatarResponse;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.dto.response.InstructorStatusResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;

import java.util.UUID;

public interface InstructorService {

    UserResponse registerInstructor(RegisterInstructorRequest request);

    InstructorResponse getCurrentProfile();

    void updateCurrentProfile(UpdateInstructorRequest request);

    AvatarResponse updateAvatar(UpdateAvatarRequest request);

    InstructorStatusResponse approveInstructor(UUID instructorId);

    InstructorStatusResponse rejectInstructor(UUID instructorId);
}
