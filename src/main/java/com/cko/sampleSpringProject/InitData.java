package com.cko.sampleSpringProject;

import com.cko.sampleSpringProject.model.Authority;
import com.cko.sampleSpringProject.model.User;
import com.cko.sampleSpringProject.service.AuthorityService;
import com.cko.sampleSpringProject.service.SMSCService;
import com.cko.sampleSpringProject.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData {

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    SMSCService smscSender;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    Faker faker = new Faker();



    public void initData() {
        initUserAndRoles();
    }

    private void initUserAndRoles(){
        Authority adminAuthority = new Authority("ROLE_ADMIN");
        Authority userAuthority = new Authority("ROLE_USER");
        authorityService.insert(adminAuthority);
        authorityService.insert(userAuthority);

        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(adminAuthority);

        for (int i = 0; i < 2; i++) {
            userService.insert(new User(i+"@mail.ru",bCryptPasswordEncoder.encode(String.valueOf(i)), authorities));
        }



    }
}
