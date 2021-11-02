package com.nekromant.zoo.mapper;

import com.nekromant.zoo.model.Room;
import dto.RoomDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RoomMapper {

    public Room dtoToEntity(RoomDTO roomDTO){
        return new Room(
                roomDTO.getId(),
                roomDTO.getAnimalType(),
                roomDTO.getRoomType(),
                roomDTO.getLocation(),
                roomDTO.getVideoSupported(),
                roomDTO.getDescription()
        );
    }

    public RoomDTO entityToDto(Room room){
        return new RoomDTO(
                room.getId(),
                room.getAnimalType(),
                room.getRoomType(),
                room.getVideoSupported(),
                room.getDescription(),
                room.getLocation(),
                LocalDate.now(),
                LocalDate.now()
        );
    }
}
