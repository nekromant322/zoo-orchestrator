package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.CallRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CallRequestDAO extends CrudRepository<CallRequest, Long> {

    @Transactional
    void deleteById(long id);

    @Override
    List<CallRequest> findAll();

    @Override
    long count();
}
