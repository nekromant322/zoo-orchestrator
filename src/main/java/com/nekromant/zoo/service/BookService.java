package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AnimalRequestService animalRequestService;

    /**
     * Auto booking by {@link AnimalRequest} id, {@link AnimalRequest} request status must be {@link RequestStatus} APPLIED
     * @param id - {@link AnimalRequest}
     * @return new {@link Book} or null
     */
    //todo razdilit na 2 vizaova perepisat' javadoc
    public Book autoBookAnimalRequest(String id){
        AnimalRequest animalRequest = animalRequestService.findById(id);
        if(animalRequest.getRequestStatus() != RequestStatus.APPLIED) return null;

        List<Room> rooms = roomService.findByAnimalRequest(animalRequest);
        if(rooms.size() == 0) return null;


        Room spareRoom = null;
        for(Room room : rooms){
            Book book = bookDAO.findSpareByRoomId(
                    room.getId(),
                    animalRequest.getEndDate(),
                    animalRequest.getBeginDate()
            ).orElse(null);
            if(book == null) spareRoom = room;
        }
        if(spareRoom == null) return null;

        Book book = new Book(
                0L,
                animalRequest.getId(),
                spareRoom.getId(),
                animalRequest.getBeginDate(),
                animalRequest.getEndDate()
                );
        bookDAO.save(book);

        animalRequestService.setInProgressAnimalRequest(id);
        return book;
    }

    public List<Book> findAll() {
        return bookDAO.findAll();
    }

//    /**
//     * Booking room with {@link AnimalRequest} id and {@link Room}
//     * {@link AnimalRequest} request status must be {@link RequestStatus} APPLIED
//     * @param id
//     * @param room
//     * @return
//     */
//    public Book bookRoom(String id, Room room) {
//        AnimalRequest animalRequest = animalRequestService.findById(id);
//        if(animalRequest.getRequestStatus() != RequestStatus.APPLIED) return null;
//
//        Book book = new Book(0L,
//                animalRequest.getId(),
//                room.getId(),
//                animalRequest.getBeginDate(),
//                animalRequest.getEndDate());
//
//        room.setEndDate(animalRequest.getEndDate());
//        roomService.insert(room);
//        animalRequestService.setInProgressAnimalRequest(id);
//        bookDAO.save(book);
//        return book;
//    }
}
