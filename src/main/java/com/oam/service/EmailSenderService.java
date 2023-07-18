package com.oam.service;

import com.oam.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(User user, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("homemanagement@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
