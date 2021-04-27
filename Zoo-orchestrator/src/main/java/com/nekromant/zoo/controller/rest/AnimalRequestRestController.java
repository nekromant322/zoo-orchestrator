package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.service.AnimalRequestService;
import com.nekromant.zoo.service.UserService;
import dto.AnimalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animalRequest")
public class AnimalRequestRestController {

    @Autowired
    private AnimalRequestService animalRequestService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public Long newRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        animalRequestService.insert(animalRequestDTO);

        Optional<AnimalRequest> animal = animalRequestService.findByName(animalRequestDTO.getName());

        return animal.map(AnimalRequest::getId).orElse(null);
    }

    @GetMapping("/onlyNew")
    public List<AnimalRequestDTO> onlyNewAnimalRequestPage(@RequestParam(name = "Spam", required = false, defaultValue = "false") Boolean spam) {
        if (!spam)
            return animalRequestService.getAllNewAnimalRequest();
        else
            return animalRequestService.getAllBlockedNewAnimalRequest();
    }

    @PostMapping("/onlyNew/accept/{id}")
    public void acceptAnimalRequestPage(@PathVariable String id) {
        animalRequestService.acceptAnimalRequest(id);
        userService.createUser(id);
    }

    @PostMapping("/onlyNew/decline/{id}")
    public void declineAnimalRequest(@PathVariable String id) {
        animalRequestService.declineAnimalRequest(id);
    }
}
