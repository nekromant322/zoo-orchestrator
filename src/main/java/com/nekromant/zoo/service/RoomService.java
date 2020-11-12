package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
     * @param animalType {@link AnimalType}
     * @param roomType {@link RoomType}
     * @param video boolean need to record in a room
     * @return
     */
    public List<Room> findByAnimalRequest(AnimalType animalType,
                                          RoomType roomType,
                                          boolean video){
        return roomDAO.findAllByAnimalRequest(
                animalType,
                roomType,
                video
        );
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    /**
     * Finds suitable rooms based on
     * @param animalType {@link AnimalType}
     * @param roomType {@link RoomType}
     * @param video - boolean need to record in a room
     * @param begin - begin date
     * @param end - end date
     * @return - List {@link Room} found
     */
    public List<Room> findAllSpareRoom(AnimalType animalType,
                                       RoomType roomType,
                                       boolean video,
                                       LocalDate begin,
                                       LocalDate end){
        List<Room> spareRooms = new ArrayList<>();
        List<Room> rooms = findByAnimalRequest(animalType, roomType, video);
        for(Room room : rooms) {
            List<Book> books = bookService.findByRoomIdAndDate(
                    String.valueOf(room.getId()),
                    begin,
                    end
            );
            if(books.size() == 0) spareRooms.add(room);
        }
        return spareRooms;
    }
}
