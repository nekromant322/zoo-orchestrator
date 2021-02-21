package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.mapper.RoomMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import dto.RoomDTO;
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

    @Autowired
    private RoomMapper roomMapper;

    public List<Book> findAll() {
        return bookDAO.findAll();
    }

    /**
     * Booking room with {@link AnimalRequest} id and {@link Room}
     * {@link AnimalRequest} request status must be {@link enums.RequestStatus} APPLIED
     * @param id - {@link AnimalRequest}
     * @param roomDTO - {@link RoomDTO} room to book
     * @return new {@link Book} or null
     */
    @Transactional
    public Book bookAnimalRequest(String id, RoomDTO roomDTO) {
        Optional<AnimalRequest> animalRequest = animalRequestService.findById(id);
        return animalRequest.map(request -> bookRoom(roomMapper.dtoToEntity(roomDTO), request)).orElse(null);
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
