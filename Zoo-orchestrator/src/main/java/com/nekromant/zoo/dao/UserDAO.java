package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User,Long> {

    User findByEmail(String email);
    User findById(long id);


}
