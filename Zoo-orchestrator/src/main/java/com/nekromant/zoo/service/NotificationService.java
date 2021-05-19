package com.nekromant.zoo.service;

import com.nekromant.zoo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;

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

    public void sendSmsWithCode(String phone) {
        Random random = new Random();
        int pin = random.nextInt((9999 - 100) + 1) + 10;
        smscService.sendSms(phone, "PIN: " + pin);
    }
}
