package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.AdvertisementMailingService;
import dto.AdvertisementMailingMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mailingPage")
public class AdvertisementMailingRestController {
    @Autowired
    private AdvertisementMailingService advertisementMailingService;

    @PostMapping
    public void sendMailing(@RequestBody AdvertisementMailingMessageDTO message) {
        advertisementMailingService.sendMailing(message);
    }

}
