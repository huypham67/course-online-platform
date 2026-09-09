package com.fullstack.online_course_platform.service;

public interface MailService {

    void sendPasswordResetEmail(String recipient, String resetToken);
}