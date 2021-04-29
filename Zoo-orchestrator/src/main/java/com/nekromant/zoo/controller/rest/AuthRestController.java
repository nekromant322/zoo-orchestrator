package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.config.security.JwtHelper;
import com.nekromant.zoo.service.CustomUserDetailService;
import com.nekromant.zoo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The auth controller to handle login requests
 */
@Slf4j
@RestController
public class AuthRestController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String login(@RequestParam String email, @RequestParam String password) {

        UserDetails userDetails;
        try {
            userDetails = customUserDetailService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.warn("User {} not found", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {

            Map<String, String> claims = new HashMap<>();
            claims.put("username", email);

            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            claims.put("authorities", authorities);

            return jwtHelper.createJwtForClaims(email, claims);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @PostMapping(path = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(@RequestParam(name = "email") String email,
                         @RequestParam(name = "password") String password) {
        userService.register(email, password);
        return login(email, password);
    }
}
