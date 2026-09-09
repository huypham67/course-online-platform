package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpMailService implements MailService {

    private final JavaMailSender mailSender;

    @Value("${app.password-reset.frontend-url}")
    private String frontendUrl;

    @Value("${app.password-reset.mail-from}")
    private String mailFrom;

    @Override
    public void sendPasswordResetEmail(String recipient, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(recipient);
        message.setSubject("Reset your Online Course Platform password");
        message.setText("Use the following link to reset your password. This link expires shortly:\n\n" + resetUrl);
        mailSender.send(message);
    }
}