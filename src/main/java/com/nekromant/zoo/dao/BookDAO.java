package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookDAO extends CrudRepository<Book,Long> {
    List<Book> findAll();
    List<Book> findAllByRoomId(long id);

    @Query("select distinct b from Book b where b.roomId = :roomId and ((b.beginDate between :begDate and :endDate) or (b.endDate between :begDate and :endDate))")
    Optional<Book> findSpareByRoomId(@Param("roomId") long id,
                                     @Param("beginDate") LocalDate beginDate,
                                     @Param("endDate") LocalDate endDate);
}
