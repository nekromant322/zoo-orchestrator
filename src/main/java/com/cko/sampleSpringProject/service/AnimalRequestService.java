package com.cko.sampleSpringProject.service;

import com.cko.sampleSpringProject.dao.AnimalRequestDAO;
import com.cko.sampleSpringProject.model.AnimalRequest;
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
