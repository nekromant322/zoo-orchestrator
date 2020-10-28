package com.nekromant.zoo.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactsController {

    @Value("${contacts.email}")
    private String EMAIL;

    @Value("${contacts.phone.number}")
    private String PHONE_NUMBER;

    @Value("${contacts.address}")
    private String ADDRESS;

    @Value("${contacts.lat}")
    private String LAT;

    @Value("${contacts.lng}")
    private String LNG;

    @Value("${map.api.key}")
    private String MAP_API_KEY;

    @GetMapping("/contacts")
    public ModelAndView contactsPage() {
        ModelAndView modelAndView = new ModelAndView("contacts");
        modelAndView.addObject("phoneNumber", PHONE_NUMBER);
        modelAndView.addObject("email", EMAIL);
        modelAndView.addObject("location", EMAIL);
        modelAndView.addObject("lat", LAT);
        modelAndView.addObject("lng", LNG);
        modelAndView.addObject("address", ADDRESS);
        modelAndView.addObject("mapApiKey", MAP_API_KEY);
        return modelAndView;
    }

}
