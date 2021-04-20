package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.AnimalRequest;
import enums.RequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRequestDAO extends CrudRepository<AnimalRequest, Long> {

    List<AnimalRequest> findAllByRequestStatus(RequestStatus requestStatus);

    Optional<AnimalRequest> findById(Long id);

    Optional<AnimalRequest> findByName(String name);
}
