package com.example.TodoListBackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetUrl){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Email from ToDo App ");
        message.setText("Click the attached link to reset your password : "+ resetUrl);

        mailSender.send(message);

    }
}
