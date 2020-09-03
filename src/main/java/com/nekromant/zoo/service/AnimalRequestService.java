package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    public void insert(AnimalRequest animalRequest) {
        animalRequestDAO.save(animalRequest);
    }
}
