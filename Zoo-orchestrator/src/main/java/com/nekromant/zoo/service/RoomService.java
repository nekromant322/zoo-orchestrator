package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.model.Room;
import dto.BookDTO;
import dto.RoomDTO;
import enums.AnimalType;
import enums.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private BookingService bookService;

    public Room insert(Room room){
        return roomDAO.save(room);
    }

    /**
     * Find List {@link Room} by -
     * @param animalType {@link enums.AnimalType}
     * @param roomType {@link enums.RoomType}
     * @param video boolean need to record in a room
     * @return
     */
    public List<Room> findByParameters(AnimalType animalType,
                                       RoomType roomType,
                                       boolean video){
        return roomDAO.findAllByParametrs(
                animalType,
                roomType,
                video
        );
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }


    /**
     * @param roomDTO - {@link RoomDTO}
     * @return List spare {@link Room} or empty list
     */
    public List<Room> findAllSpareRoom(RoomDTO roomDTO){
        List<Room> spareRooms = new ArrayList<>();
        List<Room> rooms = findByParameters(
                roomDTO.getAnimalType(),
                roomDTO.getRoomType(),
                roomDTO.getVideoSupported()
        );
        for(Room room : rooms) {
            List<BookDTO> books = bookService.findByRoomIdAndDate(
                    String.valueOf(room.getId()),
                    roomDTO.getBegin(),
                    roomDTO.getEnd()
            );
            if(books.size() == 0) spareRooms.add(room);
        }
        return spareRooms;
    }
}
