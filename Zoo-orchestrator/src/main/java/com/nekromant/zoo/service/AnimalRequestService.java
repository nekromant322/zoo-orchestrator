package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.model.AnimalRequest;
import dto.AnimalRequestDTO;
import enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private PriceService priceService;

    @Autowired
    private AnimalRequestMapper animalRequestMapper;

    @Autowired
    private BlackListDAO blackListDAO;

    public void insert(AnimalRequestDTO animalRequestDTO) {

        animalRequestDTO.setSpamRequest(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()));

        animalRequestDAO.save(animalRequestMapper.dtoToEntity(animalRequestDTO, priceService.calculateTotalPrice(animalRequestDTO)));
    }

    public List<AnimalRequestDTO> getAllNewAnimalRequest() {
        return animalRequestDAO.findAllBySpamRequest(false).stream()
                .map(animalRequestMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<AnimalRequestDTO> getAllBlockedNewAnimalRequest() {
        return animalRequestDAO.findAllBySpamRequest(true).stream()
                .map(animalRequestMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public AnimalRequest acceptAnimalRequest(String id) {
        return changeStatusAnimalRequest(id, RequestStatus.APPLIED);
    }

    public AnimalRequest declineAnimalRequest(String id) {
        return changeStatusAnimalRequest(id, RequestStatus.DENIED);
    }

    public AnimalRequest setInProgressAnimalRequest(String id) {
        return changeStatusAnimalRequest(id, RequestStatus.IN_PROGRESS);
    }

    public AnimalRequest setDoneAnimalRequest(String id) {
        return changeStatusAnimalRequest(id, RequestStatus.DONE);
    }

    private AnimalRequest changeStatusAnimalRequest(String id, RequestStatus requestStatus) {
        Optional<AnimalRequest> animalRequest = animalRequestDAO.findById(Long.parseLong(id));
        if (animalRequest.isPresent()) {
            AnimalRequest request = animalRequest.get();
            request.setRequestStatus(requestStatus);
            animalRequestDAO.save(request);
        }
        return animalRequest.orElse(null);
    }

    public Optional<AnimalRequest> findById(String id) {
        return animalRequestDAO.findById(Long.parseLong(id));
    }

    public Optional<AnimalRequest> findByName(String name) {
        return animalRequestDAO.findByName(name);
    }
}
