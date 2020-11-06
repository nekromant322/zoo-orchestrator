package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookDAO extends CrudRepository<Book,Long> {
    List<Book> findAll();
}
