package com.nekromant.zoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class сontactsService {

    @Value("${contacts.email}")
    private String email;
    @Value("${contacts.phoneNumber}")
    private int number;
    @Value("${contacts.location}")
    private String location;
    @Value("${contacts.lat}")
    private float lat1;
    @Value("${contacts.lng}")
    private String lng1;
}
