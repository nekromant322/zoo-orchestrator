package com.nekromant.zoo.service;


import com.nekromant.zoo.config.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    @Autowired
    MailSender mailSender;

    public void sendEmail(String emailReceiver, String emailSubject, String emailText){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailReceiver);
        message.setSubject(emailSubject);
        message.setText(emailText);

        mailSender.getJavaMailSender().send(message);
    }
}
