package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import com.nekromant.zoo.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Api(value = "book")
public class BookRestController {
    @Autowired
    private BookService bookService;

    @ApiOperation(
            value = "find all books in db",
            produces = "json",
            httpMethod = "GET",
            response = Book.class,
            responseContainer = "List"
    )
    @GetMapping
    public List<Book> getBooks(){
        return bookService.findAll();
    }

    @ApiOperation(
            value = "booking room with AnimalRequest id",
            produces = "json",
            httpMethod = "POST",
            response = Book.class
    )
    @PostMapping("/bookRoom/{id}")
    public Book book(@PathVariable String id, @RequestBody Room room){
        return bookService.bookAnimalRequest(id,room);
    }
}
