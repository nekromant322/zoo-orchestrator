package com.nekromant.zoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @ConditionalOnProperty(value = "email.sendRealEmails", matchIfMissing = true)
    public void sendEmail(String emailReceiver, String emailSubject, String emailText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailReceiver);
        message.setSubject(emailSubject);
        message.setText(emailText);

        mailSender.send(message);
    }
}
