package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlackListDAO extends CrudRepository<BlackList, Long> {
    @Override
    List<BlackList> findAll();
}
