package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dto.AnimalRequestDTO;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.BlackList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private BlackListService blackListService;

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

    public Iterable<AnimalRequestDTO> getAllNewAnimalRequest(){
        List<BlackList> blackList = blackListService.getAll();
        return animalRequestDAO.findAllByRequestStatus(RequestStatus.NEW)
                .stream()
                .map(AnimalRequestMapper::entityToDto)
                .map(animalRequestDTO -> {
                    for(BlackList bl : blackList){
                        if(bl.getEmail().compareTo(animalRequestDTO.getEmail()) == 0
                        || bl.getPhoneNumber().compareTo(animalRequestDTO.getPhoneNumber()) == 0) {
                            animalRequestDTO.setBanned(true);
                            break;
                        }
                    }
                    return animalRequestDTO;
                })
                .collect(Collectors.toList());
    }

    public void acceptAnimalRequest(String id) {
        changeStatusAnimalRequest(id, RequestStatus.APPLIED);
    }

    public void declineAnimalRequest(String id) {
        changeStatusAnimalRequest(id, RequestStatus.DENIED);
    }

    private void changeStatusAnimalRequest(String id, RequestStatus requestStatus) {
        Optional<AnimalRequest> animalRequest = animalRequestDAO.findById(Long.parseLong(id));
        if(animalRequest.isPresent()){
            AnimalRequest request = animalRequest.get();
            request.setRequestStatus(requestStatus);
            animalRequestDAO.save(request);
        }
    }

    public Optional<AnimalRequest> findById(String id){
        return animalRequestDAO.findById(Long.parseLong(id));
    }
}
