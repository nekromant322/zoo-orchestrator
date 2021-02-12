package com.nekromant.zoo.controller.rest;

import com.cko.zoo.dto.RoomParametersDTO;
import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomRestController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    List<Room> getAll(){
        return roomService.findAll();
    }

    @PostMapping
    Room addRoom(@RequestBody Room room){
        return roomService.insert(room);
    }

    @PostMapping("/spareRooms")
    public List<Room> getSpareRoom(@RequestBody RoomParametersDTO roomParametersDTO){
        return roomService.findAllSpareRoom(roomParametersDTO);
    }
}
