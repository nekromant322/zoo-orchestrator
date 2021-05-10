package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The auth controller to handle login requests
 */
@Slf4j
@RestController
public class AuthRestController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String login(@RequestParam(name = "email") String email,
                        @RequestParam(name = "password") String password) {
        return registrationService.login(email, password);
    }

    @PostMapping(path = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(@RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password) {
        registrationService.register(email, password);
        return login(email, password);
    }

    @PostMapping(path = "/changePassword")
    public void changePassword(@RequestParam String email, @RequestParam String oldPassword,
                               @RequestParam String newPassword) {
        registrationService.changePassword(email, oldPassword, newPassword);
    }

    @PostMapping(path = "/confirmReg")
    public String confirmReg(@RequestParam String token, @RequestParam String password) {
        return registrationService.confirmReg(token, password);
    }
}
