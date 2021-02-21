package com.nekromant.zoo.controller.rest;


import com.nekromant.zoo.service.SMSCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/smsPage")
public class SMSCRestController {

    @Autowired
    private SMSCService smscService;

    @GetMapping("/send")
    public void send(){
        smscService.send("+79775548911","I'm testing my service");
    }

}
