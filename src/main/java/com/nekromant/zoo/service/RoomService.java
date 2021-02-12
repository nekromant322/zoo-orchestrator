package com.nekromant.zoo.service;

import com.cko.zoo.dto.RoomParametersDTO;
import com.cko.zoo.enums.AnimalType;
import com.cko.zoo.enums.RoomType;
import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private BookService bookService;

    public Room insert(Room room){
        return roomDAO.save(room);
    }

    /**
     * Find List {@link Room} by -
     * @param animalType {@link com.cko.zoo.enums.AnimalType}
     * @param roomType {@link com.cko.zoo.enums.RoomType}
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
     * @param roomParametersDTO - {@link RoomParametersDTO}
     * @return List spare {@link Room} or empty list
     */
    public List<Room> findAllSpareRoom(RoomParametersDTO roomParametersDTO){
        List<Room> spareRooms = new ArrayList<>();
        List<Room> rooms = findByParameters(
                roomParametersDTO.getAnimalType(),
                roomParametersDTO.getRoomType(),
                roomParametersDTO.isVideoSupported()
        );
        for(Room room : rooms) {
            List<Book> books = bookService.findByRoomIdAndDate(
                    String.valueOf(room.getId()),
                    roomParametersDTO.getBegin(),
                    roomParametersDTO.getEnd()
            );
            if(books.size() == 0) spareRooms.add(room);
        }
        return spareRooms;
    }
}
