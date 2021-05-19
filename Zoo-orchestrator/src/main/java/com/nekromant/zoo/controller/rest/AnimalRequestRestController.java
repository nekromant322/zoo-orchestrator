package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.service.AnimalRequestService;
import com.nekromant.zoo.service.NotificationService;
import com.nekromant.zoo.service.UserService;
import dto.AnimalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/animalRequest")
public class AnimalRequestRestController {

    @Autowired
    private AnimalRequestService animalRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/confirm")
    public Long confirmRequest(@RequestBody Map<String, String> payload) {
        if (payload.containsKey("phoneNumber"))
            notificationService.sendSmsWithCode(payload.get("phoneNumber"));
        return 10L;
    }

    @PostMapping()
    public Long newRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        animalRequestService.insert(animalRequestDTO);

        Optional<AnimalRequest> animal = animalRequestService.findByName(animalRequestDTO.getName());

        return animal.map(AnimalRequest::getId).orElse(null);
    }

    @GetMapping("/onlyNew")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AnimalRequestDTO> onlyNewAnimalRequest(@RequestParam(name = "Spam", required = false, defaultValue = "false") Boolean spam) {
        if (!spam)
            return animalRequestService.getAllNewAnimalRequest();
        else
            return animalRequestService.getAllBlockedNewAnimalRequest();
    }

    @PostMapping("/onlyNew/accept/{id}")
    public void acceptAnimalRequest(@PathVariable String id) {
        animalRequestService.acceptAnimalRequest(id);
        userService.createUser(id);
    }

    @PostMapping("/onlyNew/decline/{id}")
    public void declineAnimalRequest(@PathVariable String id) {
        animalRequestService.declineAnimalRequest(id);
    }
}
