package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private AnimalRequestService animalRequestService;

    public List<Book> findAll() {
        return bookDAO.findAll();
    }

    /**
     * Booking room with {@link AnimalRequest} id and {@link Room}
     * {@link AnimalRequest} request status must be {@link RequestStatus} APPLIED
     * @param id - {@link AnimalRequest}
     * @param room - {@link Room} room to book
     * @return new {@link Book} or null
     */
    @Transactional
    public Book bookAnimalRequest(String id, Room room) {
        Optional<AnimalRequest> animalRequest = animalRequestService.findById(id);
        return animalRequest.map(request -> bookRoom(room, request)).orElse(null);
    }

    private Book bookRoom(Room room, AnimalRequest animalRequest) {
        Book book = new Book(
                0L,
                animalRequest.getId(),
                room.getId(),
                animalRequest.getBeginDate(),
                animalRequest.getEndDate()
        );
        bookDAO.save(book);

        animalRequestService.setInProgressAnimalRequest(animalRequest.getId().toString());

        return book;
    }

    public List<Book> findByRoomIdAndDate(String id, LocalDate begin, LocalDate end){
        return bookDAO.findBookByRoomIdAndDate(
                Long.parseLong(id),
                begin,
                end
        );
    }
}
