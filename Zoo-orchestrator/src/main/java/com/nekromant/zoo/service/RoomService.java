package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import dto.RoomDTO;
import enums.AnimalType;
import enums.Location;
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
    private BookService bookService;

    public Room insert(Room room) {
        return roomDAO.save(room);
    }

    /**
     * Find List {@link Room} by -
     *
     * @param animalType {@link enums.AnimalType}
     * @param roomType   {@link enums.RoomType}
     * @param video      boolean need to record in a room
     * @return list of rooms, metching parameters
     */
    public List<Room> findByParameters(AnimalType animalType,
                                       RoomType roomType,
                                       Location location,
                                       boolean video) {
        return roomDAO.findDistinctByAnimalTypeAndRoomTypeAndLocationAndVideoSupported(
                animalType,
                roomType,
                location,
                video
        );
    }


    /**
     * Find List {@link Room} by -
     *
     * @param animalType {@link enums.AnimalType}
     * @param roomType   {@link enums.RoomType}
     * @return list of rooms, metching parameters
     */
    public List<Room> findByParameters(AnimalType animalType,
                                       RoomType roomType,
                                       Location location) {
        return roomDAO.findDistinctByAnimalTypeAndRoomTypeAndLocation(
                animalType,
                roomType,
                location
        );
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    /**
     * @param roomDTO - {@link RoomDTO}
     * @return List spare {@link Room} or empty list
     */
    public List<Room> findAllSpareRoom(RoomDTO roomDTO) {
        List<Room> spareRooms = new ArrayList<>();
        List<Room> rooms;
        if (roomDTO.getVideoSupported()) {
            rooms = findByParameters(roomDTO.getAnimalType(), roomDTO.getRoomType(), roomDTO.getLocation(), true);
        } else {
            rooms = findByParameters(roomDTO.getAnimalType(), roomDTO.getRoomType(), roomDTO.getLocation());
        }
        for (Room room : rooms) {
            List<Book> books = bookService.findByRoomIdAndDate(
                    String.valueOf(room.getId()),
                    roomDTO.getBegin(),
                    roomDTO.getEnd()
            );
            if (books.size() == 0) spareRooms.add(room);
        }
        return spareRooms;
    }

    public Room findRoomById(long id) {
        return roomDAO.findById(id).orElseGet(() -> null);
    }
}
