package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    public void insert(AnimalRequest animalRequest) {
        animalRequestDAO.save(animalRequest);
    }

    public HashMap<Month, Integer> getNumbersOfDoneRequestForYear(int year) {
        return animalRequestDAO.findAllByRequestStatus(RequestStatus.DONE).stream()
                .filter(rq -> rq.getBeginDate().getYear() == year)
                .collect(HashMap::new,
                        (map, value) -> map.merge(value.getBeginDate().getMonth(), 1, Integer::sum),
                        HashMap::putAll);
    }

    //TODO реализация после того, как будет реализован прайс
    public HashMap<Month, Integer> getMoneyYearnedForYear(int year) {
        return new HashMap<>();
    }

    public Iterable<AnimalRequest> getAllNewAnimalRequest(){
        return animalRequestDAO.findAllByRequestStatus(RequestStatus.NEW);
    }
}
