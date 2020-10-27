package com.nekromant.zoo.controller;

import com.nekromant.zoo.enums.RequestStatus;
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
@RequestMapping("/AnimalRequest")
public class AnimalRequestController {
    @Autowired
    AnimalRequestService animalRequestService;

    @GetMapping("/onlyNew")
    public ModelAndView onlyNewAnimalRequestPage(){
        ModelAndView mav = new ModelAndView("admin/newAnimalRequestPage");
        mav.addObject("animalRequests", animalRequestService.getAllNewAnimalRequest());
        return mav;
    }

    @PostMapping("/onlyNew/accept/{id}")
    public RedirectView acceptAnimalRequestPage(@PathVariable String id){
        animalRequestService.acceptAnimalRequest(id);
        return new RedirectView("/AnimalRequest/onlyNew");
    }

    @PostMapping("/onlyNew/decline/{id}")
    public RedirectView declineAnimalRequest(@PathVariable String id){
        animalRequestService.declineAnimalRequest(id);
        return new RedirectView("/AnimalRequest/onlyNew");
    }
}
