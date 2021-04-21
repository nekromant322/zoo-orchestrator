package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.AnimalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/AnimalRequestPage")
public class AnimalRequestController {

    @Autowired
    private AnimalRequestService animalRequestService;

    @GetMapping("/onlyNew")
    public ModelAndView onlyNewAnimalRequestPage(@RequestParam(name = "Type", required = false, defaultValue = "CLEAR") String type) {
        ModelAndView modelAndView = new ModelAndView("admin/newAnimalRequestPage");
        if (type.equals("CLEAR"))
            modelAndView.addObject("animalRequests", animalRequestService.getAllNewAnimalRequest());
        else
            modelAndView.addObject("animalRequests", animalRequestService.getAllBlockedNewAnimalRequest());
        return modelAndView;
    }

    @PostMapping("/onlyNew/accept/{id}")
    public RedirectView acceptAnimalRequestPage(@PathVariable String id) {
        animalRequestService.acceptAnimalRequest(id);
        return new RedirectView("/AnimalRequestPage/onlyNew");
    }

    @PostMapping("/onlyNew/decline/{id}")
    public RedirectView declineAnimalRequest(@PathVariable String id) {
        animalRequestService.declineAnimalRequest(id);
        return new RedirectView("/AnimalRequestPage/onlyNew");
    }
}
