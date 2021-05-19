package com.nekromant.zoo.service;

import com.nekromant.zoo.client.ConfirmationZooClient;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.User;
import dto.AnimalRequestDTO;
import dto.SMSCodeDTO;
import enums.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimalRequestService {

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private PriceService priceService;

    @Autowired
    private AnimalRequestMapper animalRequestMapper;

    @Autowired
    private BlackListDAO blackListDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationZooClient confirmationZooClient;

    public void insert(AnimalRequestDTO animalRequestDTO) {

        animalRequestDTO.setSpamRequest(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()));

        animalRequestDAO.save(animalRequestMapper.dtoToEntity(animalRequestDTO, priceService.calculateTotalPrice(animalRequestDTO)));
    }

    public void insert(AnimalRequestDTO animalRequestDTO, SMSCodeDTO smsCodeDTO) {
        animalRequestDTO.setSpamRequest(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()));
        confirmationZooClient.verifySMSCode(smsCodeDTO);
        animalRequestDAO.save(animalRequestMapper.dtoToEntity(animalRequestDTO, priceService.calculateTotalPrice(animalRequestDTO)));
    }

    public List<AnimalRequestDTO> getAllNewAnimalRequest() {
        return animalRequestDAO.findAllBySpamRequest(false).stream()
                .filter(item -> item.getRequestStatus().equals(RequestStatus.NEW))
                .map(animalRequestMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<AnimalRequestDTO> getAllBlockedNewAnimalRequest() {
        return animalRequestDAO.findAllBySpamRequest(true).stream()
                .filter(item -> item.getRequestStatus().equals(RequestStatus.NEW))
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

    /**
     * Связка пользователя {@link User} с заявкой {@link AnimalRequest}
     *
     * @param request - заявка {@link AnimalRequest}
     */
    public void bindUserAndAnimalRequest(AnimalRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (user == null || user.getId() == null) {
            throw new UsernameNotFoundException("Пользователь не найден! Заявка " + request.getId()
                    + " не была прикреплена к пользователю " + request.getEmail());
        }

        List<AnimalRequest> requestIdList = user.getAnimalRequests();
        requestIdList.add(request);

        userService.insert(user);
        log.info("Заявка {} успешно прикреплена к пользователю {}", request.getId(), request.getEmail());
    }

    public Long getCode(SMSCodeDTO smsCodeDTO) {
        return confirmationZooClient.createSMSCode(smsCodeDTO);
    }
}
