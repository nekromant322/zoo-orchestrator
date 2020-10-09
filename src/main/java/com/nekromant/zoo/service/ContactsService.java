package com.nekromant.zoo.service;

import com.nekromant.zoo.controller.ContactsController;
import org.asynchttpclient.ClientStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    HashMap<String, String> contactsInfo = new HashMap<String, String>();


//    public List<ContactsService> findAll() {
//        return this.contactsRepository.findAll();
//    }



//    contactsInfo.put()
//    public HashMap<String, String> getContactsInfo() {
//
//    }
}
