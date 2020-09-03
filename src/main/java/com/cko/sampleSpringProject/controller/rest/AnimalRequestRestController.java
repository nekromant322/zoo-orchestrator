package com.cko.sampleSpringProject.controller.rest;

import com.cko.sampleSpringProject.model.AnimalRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animalRequest")
public class AnimalRequestRestController {

    @PostMapping
    public void newRequest(AnimalRequest animalRequest) {

    }
}
