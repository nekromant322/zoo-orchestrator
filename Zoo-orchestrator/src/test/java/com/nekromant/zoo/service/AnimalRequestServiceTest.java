package com.nekromant.zoo.service;

import com.nekromant.zoo.client.ConfirmationZooClient;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.service.util.AnimalRequestUtil;
import dto.AnimalRequestDTO;
import dto.SMSCodeDTO;
import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class AnimalRequestServiceTest {
    @InjectMocks
    AnimalRequestService animalRequestService;

    @Mock
    private BlackListDAO blackListDAO;

    @Mock
    private AnimalRequestDAO animalRequestDAO;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private AnimalRequestMapper animalRequestMapper;

    @Mock
    private PriceService priceService;

    @Mock
    private ConfirmationZooClient confirmationZooClient;

    @Test
    public void insertWhenRequestIsSpam() {
        AnimalRequestDTO animalRequestDTO = createDefaultAnimalRequestDTO();

        Mockito.when(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()))
                .thenReturn(true);
        Mockito.when(priceService.calculateTotalPrice(animalRequestDTO)).thenReturn(100);

        animalRequestService.insert(animalRequestDTO);

        Mockito.verify(animalRequestDAO).save(AnimalRequestUtil.createAnimalRequest(true));

        animalRequestService.insert(animalRequestDTO, new SMSCodeDTO());

        Mockito.verify(animalRequestDAO, times(2)).save(AnimalRequestUtil.createAnimalRequest(true));
    }

    @Test
    public void insertWhenRequestIsNotSpam() {
        AnimalRequestDTO animalRequestDTO = createDefaultAnimalRequestDTO();

        Mockito.when(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()))
                .thenReturn(false);
        Mockito.when(priceService.calculateTotalPrice(animalRequestDTO)).thenReturn(100);

        animalRequestService.insert(animalRequestDTO);

        Mockito.verify(animalRequestDAO).save(AnimalRequestUtil.createAnimalRequest(false));
    }

    @Test
    public void getAllNewAnimalRequest() {
        List<AnimalRequest> data = new LinkedList<>(Arrays.asList(AnimalRequestUtil.createAnimalRequest(false),
                AnimalRequestUtil.createAnimalRequest(false), AnimalRequestUtil.createAnimalRequest(false)));
        Mockito.when(animalRequestDAO.findAllBySpamRequest(Mockito.any())).thenReturn(data);

        List<AnimalRequestDTO> result = animalRequestService.getAllNewAnimalRequest();

        Assert.assertEquals(result.size(), 3);
    }

    @Test
    public void getAllBlockedNewAnimalRequest() {
        List<AnimalRequest> data = new LinkedList<>(Arrays.asList(AnimalRequestUtil.createAnimalRequest(true),
                AnimalRequestUtil.createAnimalRequest(true), AnimalRequestUtil.createAnimalRequest(true)));
        Mockito.when(animalRequestDAO.findAllBySpamRequest(Mockito.any())).thenReturn(data);

        List<AnimalRequestDTO> result = animalRequestService.getAllBlockedNewAnimalRequest();

        Assert.assertEquals(result.size(), 3);
    }

    @Test
    public void acceptAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(AnimalRequestUtil.createAnimalRequest(false, RequestStatus.NEW));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.acceptAnimalRequest(id);

        Assert.assertEquals(RequestStatus.APPLIED, result.getRequestStatus());
    }

    @Test
    public void declineAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(AnimalRequestUtil.createAnimalRequest(false, RequestStatus.NEW));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.declineAnimalRequest(id);

        Assert.assertEquals(RequestStatus.DENIED, result.getRequestStatus());
    }

    @Test
    public void setInProgressAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(AnimalRequestUtil.createAnimalRequest(false, RequestStatus.APPLIED));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.setInProgressAnimalRequest(id);

        Assert.assertEquals(RequestStatus.IN_PROGRESS, result.getRequestStatus());
    }

    @Test
    public void setDoneAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(AnimalRequestUtil.createAnimalRequest(false, RequestStatus.IN_PROGRESS));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.setDoneAnimalRequest(id);

        Assert.assertEquals(RequestStatus.DONE, result.getRequestStatus());
    }

    private AnimalRequestDTO createDefaultAnimalRequestDTO() {
        return new AnimalRequestDTO(
                1L,
                RequestStatus.NEW,
                AnimalType.DOG,
                LocalDate.now(),
                LocalDate.now(),
                RoomType.VIP,
                true,
                "+7(999)-(999)-(99)-(99)",
                "test@email.com",
                "name",
                "surname",
                "dog-dog",
                Location.MOSCOW,
                false
        );
    }

}