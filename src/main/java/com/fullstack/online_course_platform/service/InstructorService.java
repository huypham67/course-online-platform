package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;

import java.util.UUID;

public interface InstructorService {

    UserResponse registerInstructor(RegisterInstructorRequest request);

    InstructorResponse getCurrentProfile();

    InstructorResponse updateCurrentProfile(UpdateInstructorRequest request);

    InstructorResponse approveInstructor(UUID instructorId);

    InstructorResponse rejectInstructor(UUID instructorId);
}
