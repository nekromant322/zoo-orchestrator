package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.AnimalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/AnimalRequest")
public class AnimalRequestController {
    @Autowired
    AnimalRequestService animalRequestService;

    @GetMapping("/onlyNew")
    public ModelAndView onlyNewAnimalRequestPage(){
        ModelAndView mav = new ModelAndView("newAnimalRequestPage");
        mav.addObject("animalRequests", animalRequestService.getAllNewAnimalRequest());
        return mav;
    }
}
