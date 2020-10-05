package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    public AnimalRequest insert(AnimalRequest animalRequest) {
        return animalRequestDAO.save(animalRequest);
    }

    public HashMap<Month, Integer> getNumbersOfDoneRequestForYear(int year) {
        return animalRequestDAO.findAllByRequestStatus(RequestStatus.DONE).stream()
                .filter(rq -> rq.getBeginDate().getYear() == year)
                .collect(HashMap::new,
                        (map, value) -> map.merge(value.getBeginDate().getMonth(), 1, Integer::sum),
                        HashMap::putAll);
    }

    public void updateRoomTypeByRequestId(long id, RoomType roomType) {
        Optional<AnimalRequest> optionalAnimalRequest = animalRequestDAO.findById(id);
        if (optionalAnimalRequest.isPresent()) {
            AnimalRequest animalRequest = optionalAnimalRequest.get();
            animalRequest.setRoomType(roomType);
            animalRequestDAO.save(animalRequest);
        }
    }

    //TODO реализация после того, как будет реализован прайс
    public HashMap<Month, Integer> getMoneyYearnedForYear(int year) {
        return new HashMap<>();
    }
}
