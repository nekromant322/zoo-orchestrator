package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookDAO extends CrudRepository<Book,Long> {
    List<Book> findAll();

    @Query("select b from Book b where b.roomId = :roomId and ((:endDate between b.beginDate and b.endDate) or " +
            "(:beginDate between b.beginDate and b.endDate) or " +
            "((:beginDate < b.beginDate) and (:endDate > b.endDate)))")
    List<Book> findBookByRoomIdAndDate(@Param("roomId") long id,
                                     @Param("beginDate") LocalDate beginDate,
                                     @Param("endDate") LocalDate endDate);
}
