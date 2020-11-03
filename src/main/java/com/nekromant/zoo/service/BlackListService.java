package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.BlackList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlackListService {
    @Autowired
    BlackListDAO blackListDAO;

    @Autowired
    AnimalRequestService animalRequestService;

    public void insert(BlackList blackList){
        blackListDAO.save(blackList);
    }

    public void insertById(String id) {
        Optional<AnimalRequest> animalRequest = animalRequestService.findById(id);
        blackListDAO.save(new BlackList(0,animalRequest.get().getEmail(),animalRequest.get().getPhoneNumber()));
        animalRequestService.declineAnimalRequest(id);
    }
}
