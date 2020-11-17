package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.dto.RoomParametersDTO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@Api(value = "room")
public class RoomRestController {
    @Autowired
    private RoomService roomService;

    @ApiOperation(
            value = "get all rooms from db",
            response = Room.class,
            responseContainer = "List",
            httpMethod = "GET",
            produces = "json"
    )
    @GetMapping
    List<Room> getAll(){
        return roomService.findAll();
    }

    @ApiOperation(
            value = "add room to db from request body",
            response = Room.class,
            httpMethod = "POST",
            produces = "json"
    )
    @PostMapping
    Room addRoom(@RequestBody Room room){
        return roomService.insert(room);
    }

    @ApiOperation(
            value = "get rooms by parameters",
            notes = "find by Parameters in RoomParametersDTO such as AnimalType, RoomType, videoNeeded begin-end date",
            response = Room.class,
            responseContainer = "List",
            httpMethod = "POST",
            produces = "json"
    )
    @PostMapping("/spareRooms")
    public List<Room> getSpareRoom(@RequestBody RoomParametersDTO roomParametersDTO){
        return roomService.findAllSpareRoom(roomParametersDTO);
    }
}
