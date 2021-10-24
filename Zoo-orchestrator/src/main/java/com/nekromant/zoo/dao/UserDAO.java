package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface UserDAO extends CrudRepository<User,Long> {

    User findByEmail(String email);
    User findById(long id);

    Page<User> findAllByLastActionAfter(LocalDate date, Pageable pageable);
}
