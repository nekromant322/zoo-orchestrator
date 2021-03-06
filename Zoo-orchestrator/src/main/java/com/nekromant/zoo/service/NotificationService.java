package com.nekromant.zoo.service;

import com.nekromant.zoo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private SMSCService smscService;

    public void sendEmail(User user, String emailSubject, String message) {
        emailService.sendEmail(user.getEmail(), emailSubject, message);
    }

    public void sendSms(User user, String message) {
        smscService.sendSms(user.getPhoneNumber(), message);
    }
}
