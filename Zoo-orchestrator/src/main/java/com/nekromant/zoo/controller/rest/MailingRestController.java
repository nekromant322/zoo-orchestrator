package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.MailingService;
import dto.MailingMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mailingPage")
public class MailingRestController {
    @Autowired
    private MailingService mailingService;

    @PostMapping
    public void sendMailing(@RequestBody MailingMessageDTO message) {
        mailingService.sendMailing(message);
    }

}
