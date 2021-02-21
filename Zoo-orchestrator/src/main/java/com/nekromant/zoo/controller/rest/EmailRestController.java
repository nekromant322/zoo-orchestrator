package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/emailPage")
@RestController
public class EmailRestController {

    @Autowired
    EmailService emailService;

}
