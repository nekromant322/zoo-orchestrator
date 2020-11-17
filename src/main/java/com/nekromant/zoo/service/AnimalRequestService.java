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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private AnimalRequestMapper animalRequestMapper;

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
        List<AnimalRequest> animalRequestList = animalRequestDAO.findAllByRequestStatus(RequestStatus.NEW);
        List<AnimalRequestDTO> dtoList = new ArrayList<>();
        for(AnimalRequest animalRequest : animalRequestList){
            AnimalRequestDTO dto = animalRequestMapper.entityToDto(animalRequest);
            for(BlackList bl : blackList){
                if(bl.getEmail().compareTo(animalRequest.getEmail()) == 0
                 || bl.getPhoneNumber().compareTo(animalRequest.getPhoneNumber()) == 0){
                    dto.setBanned(true);
                    break;
                }
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public void acceptAnimalRequest(String id) {
        changeStatusAnimalRequest(id, RequestStatus.APPLIED);
    }

    public void declineAnimalRequest(String id) {
        changeStatusAnimalRequest(id, RequestStatus.DENIED);
    }

    public boolean setInProgressAnimalRequest(String id){
        changeStatusAnimalRequest(id,RequestStatus.IN_PROGRESS);
        return true;
    }

    public void setDoneAnimalRequest(String id){
        changeStatusAnimalRequest(id,RequestStatus.DONE);
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
