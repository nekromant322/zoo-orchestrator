package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.BlackList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BlackListService {
    @Autowired
    private BlackListDAO blackListDAO;

    @Autowired
    private AnimalRequestService animalRequestService;


    /**
     * Find animalRequest by id
     * Creates Black list with email and phone number animalRequest found
     *
     * @param id primary index of animalRequest
     */
    public void insertByAnimalRequestId(String id) {
        Optional<AnimalRequest> animalRequest = animalRequestService.findById(id);
        if (animalRequest.isPresent()) {
            blackListDAO.save(new BlackList(0, animalRequest.get().getEmail(), animalRequest.get().getPhoneNumber()));
            animalRequestService.declineAnimalRequest(id);
            log.info("Номер телефона: {} и email: {} добавлены в чёрный список", animalRequest.get().getPhoneNumber(), animalRequest.get().getEmail());
        }
    }

    /**
     * @return List {@link BlackList} - all BlackLists
     */
    public List<BlackList> getAll() {
        return blackListDAO.findAll();
    }
}
