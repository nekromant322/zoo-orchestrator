package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.mapper.RoomMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.AnimalRequestService;
import com.nekromant.zoo.service.BookService;
import com.nekromant.zoo.service.RoomService;
import com.nekromant.zoo.service.UserService;
import dto.AnimalRequestDTO;
import dto.SMSCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AnimalRequestMapper animalRequestMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private BookService bookService;

    @PostMapping()
    public Long newRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        animalRequestService.insert(animalRequestDTO);

        Optional<AnimalRequest> animal = animalRequestService.findByName(animalRequestDTO.getName());

        return animal.map(AnimalRequest::getId).orElse(null);
    }

    @PostMapping("/confirm")
    public Long confirmRequest(@RequestBody SMSCodeDTO smsCodeDTO) {
        return animalRequestService.getCode(smsCodeDTO);
    }

    @PostMapping("/create")
    public Long createRequest(@RequestBody AnimalRequestDTO.NewAnimalRequestDTO requestDTO) {
        AnimalRequestDTO animalRequestDTO = animalRequestMapper.newAnimalRequestDTOToAnimalRequestDTO(requestDTO);
        SMSCodeDTO smsCodeDTO = new SMSCodeDTO(requestDTO.getCode(), requestDTO.getPhoneNumber());
        animalRequestService.insert(animalRequestDTO, smsCodeDTO);
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

    @PostMapping("/onlyNew/accept")
    public void acceptAnimalRequest(@RequestBody AnimalRequestDTO.AcceptAnimalRequestDTO requestDTO) {
        animalRequestService.acceptAnimalRequest(String.valueOf(requestDTO.getAnimalRequestId()));
        Room room = roomService.findRoomById(requestDTO.getRoomId());
        bookService.bookAnimalRequest(String.valueOf(requestDTO.getAnimalRequestId()), roomMapper.entityToDto(room));
        userService.createUser(String.valueOf(requestDTO.getAnimalRequestId()));
    }

    @PostMapping("/onlyNew/decline/{id}")
    public void declineAnimalRequest(@PathVariable String id) {
        animalRequestService.declineAnimalRequest(id);
    }
}
