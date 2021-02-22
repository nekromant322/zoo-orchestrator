package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.AnimalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/AnimalRequestPage")
public class AnimalRequestController {
    
    @Autowired
    private AnimalRequestService animalRequestService;

    @GetMapping("/onlyNew")
    public ModelAndView onlyNewAnimalRequestPage(){
        ModelAndView modelAndView = new ModelAndView("admin/newAnimalRequestPage");
        modelAndView.addObject("animalRequests", animalRequestService.getAllNewAnimalRequest());
        return modelAndView;
    }

    @PostMapping("/onlyNew/accept/{id}")
    public RedirectView acceptAnimalRequestPage(@PathVariable String id){
        animalRequestService.acceptAnimalRequest(id);
        return new RedirectView("/AnimalRequestPage/onlyNew");
    }

    @PostMapping("/onlyNew/decline/{id}")
    public RedirectView declineAnimalRequest(@PathVariable String id){
        animalRequestService.declineAnimalRequest(id);
        return new RedirectView("/AnimalRequestPage/onlyNew");
    }
}
