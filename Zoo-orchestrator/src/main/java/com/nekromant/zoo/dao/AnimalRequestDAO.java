package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.AnimalRequest;
import enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnimalRequestDAO extends CrudRepository<AnimalRequest, Long> {

    List<AnimalRequest> findAllByRequestStatus(RequestStatus requestStatus);

    List<AnimalRequest> findAllBySpamRequest(Boolean spamRequest);

    Optional<AnimalRequest> findById(Long id);

    Optional<AnimalRequest> findByName(String name);

    Page<AnimalRequest> findAllByBeginDateAfter(LocalDate date, Pageable pageable);

}
