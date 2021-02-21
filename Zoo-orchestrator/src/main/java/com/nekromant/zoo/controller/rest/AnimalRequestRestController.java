package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.AnimalRequestService;
import dto.AnimalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/animalRequestPage")
public class AnimalRequestRestController {

    @Autowired
    private AnimalRequestService animalRequestService;

    @PostMapping
    public void newRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        animalRequestService.insert(animalRequestDTO);
    }
}
