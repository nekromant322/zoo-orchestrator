package com.nekromant.zoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContactsService {

    @Value("${contacts.email}")
    private String EMAIL;
    @Value("${contacts.phone.number}")
    private String PHONE_NUMBER;
    @Value("${contacts.location}")
    private String LOCATION;
    @Value("${contacts.lat}")
    private float LAT;
    @Value("${contacts.lng}")
    private float LNG;
}
