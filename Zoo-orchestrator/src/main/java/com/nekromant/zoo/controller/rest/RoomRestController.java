package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.RoomService;
import dto.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomPage")
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
    public List<Room> getSpareRoom(@RequestBody RoomDTO roomDTO){
        return roomService.findAllSpareRoom(roomDTO);
    }
}
