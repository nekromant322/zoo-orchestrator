package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booksPage")
public class BookRestController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getBooks(){
        return bookService.findAll();
    }

    @PostMapping("/bookRoom/{id}")
    public Book book(@PathVariable String id, @RequestBody Room room){
        return bookService.bookAnimalRequest(id,room);
    }
}
