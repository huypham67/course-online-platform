package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.ForgotPasswordRequest;
import com.fullstack.online_course_platform.dto.request.ResetPasswordRequest;

public interface PasswordResetService {

    void requestPasswordReset(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}