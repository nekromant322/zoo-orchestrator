package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.BookingService;
import dto.BookDTO;
import dto.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booksPage")
public class BookRestController {
    @Autowired
    private BookingService bookService;

    @GetMapping
    public List<BookDTO> getBooks(){
        return bookService.findAll();
    }

    @PostMapping("/bookRoom/{id}")
    public void bookKafka(@PathVariable String id, @RequestBody RoomDTO roomDTO){
        bookService.bookAnimalRequest(id,roomDTO);
    }
}
