package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.mapper.AnimalRequestMapper;
import com.nekromant.zoo.model.AnimalRequest;
import dto.AnimalRequestDTO;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void insertWhenRequestIsSpam() {
        AnimalRequestDTO animalRequestDTO = createDefaultAnimalRequestDTO();

        Mockito.when(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()))
                .thenReturn(true);
        Mockito.when(priceService.calculateTotalPrice(animalRequestDTO)).thenReturn(100);

        animalRequestService.insert(animalRequestDTO);

        Mockito.verify(animalRequestDAO).save(createAnimalRequest(true));
    }

    @Test
    public void insertWhenRequestIsNotSpam() {
        AnimalRequestDTO animalRequestDTO = createDefaultAnimalRequestDTO();

        Mockito.when(blackListDAO.existsByPhoneNumberOrEmailIgnoreCase(animalRequestDTO.getPhoneNumber(), animalRequestDTO.getEmail()))
                .thenReturn(false);
        Mockito.when(priceService.calculateTotalPrice(animalRequestDTO)).thenReturn(100);

        animalRequestService.insert(animalRequestDTO);

        Mockito.verify(animalRequestDAO).save(createAnimalRequest(false));
    }

    @Test
    public void getNumbersOfDoneRequestForYear() {
        int year = 2021;
        List<AnimalRequest> data = new LinkedList<>();
        data.add(createAnimalRequest(LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 14)));
        data.add(createAnimalRequest(LocalDate.of(2021, 1, 10), LocalDate.of(2021, 2, 10)));
        data.add(createAnimalRequest(LocalDate.of(2021, 4, 10), LocalDate.of(2021, 4, 20)));
        Mockito.when(animalRequestDAO.findAllByRequestStatus(Mockito.any())).thenReturn(data);

        HashMap<Month, Integer> result = animalRequestService.getNumbersOfDoneRequestForYear(year);

        Assert.assertEquals(result.get(Month.JANUARY), Integer.valueOf(2));
        Assert.assertEquals(result.get(Month.APRIL), Integer.valueOf(1));
    }

    @Test
    public void getAllNewAnimalRequest() {
        List<AnimalRequest> data = new LinkedList<>(Arrays.asList(createAnimalRequest(false),
                createAnimalRequest(false), createAnimalRequest(false)));
        Mockito.when(animalRequestDAO.findAllBySpamRequest(Mockito.any())).thenReturn(data);

        List<AnimalRequestDTO> result = animalRequestService.getAllNewAnimalRequest();

        Assert.assertEquals(result.size(), 3);
    }

    @Test
    public void getAllBlockedNewAnimalRequest() {
        List<AnimalRequest> data = new LinkedList<>(Arrays.asList(createAnimalRequest(true),
                createAnimalRequest(true), createAnimalRequest(true)));
        Mockito.when(animalRequestDAO.findAllBySpamRequest(Mockito.any())).thenReturn(data);

        List<AnimalRequestDTO> result = animalRequestService.getAllBlockedNewAnimalRequest();

        Assert.assertEquals(result.size(), 3);
    }

    @Test
    public void acceptAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(createAnimalRequest(false, RequestStatus.NEW));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.acceptAnimalRequest(id);

        Assert.assertEquals(RequestStatus.APPLIED, result.getRequestStatus());
    }

    @Test
    public void declineAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(createAnimalRequest(false, RequestStatus.NEW));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.declineAnimalRequest(id);

        Assert.assertEquals(RequestStatus.DENIED, result.getRequestStatus());
    }

    @Test
    public void setInProgressAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(createAnimalRequest(false, RequestStatus.APPLIED));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.setInProgressAnimalRequest(id);

        Assert.assertEquals(RequestStatus.IN_PROGRESS, result.getRequestStatus());
    }

    @Test
    public void setDoneAnimalRequest() {
        String id = "1";
        Optional<AnimalRequest> data = Optional.of(createAnimalRequest(false, RequestStatus.IN_PROGRESS));
        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(data);

        AnimalRequest result = animalRequestService.setDoneAnimalRequest(id);

        Assert.assertEquals(RequestStatus.DONE, result.getRequestStatus());
    }

    private AnimalRequest createAnimalRequest() {
        return new AnimalRequest(
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
                100,
                Location.MOSCOW,
                false
        );
    }

    private AnimalRequest createAnimalRequest(boolean spam) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setSpamRequest(spam);
        return animalRequest;
    }

    private AnimalRequest createAnimalRequest(LocalDate beginDate, LocalDate endDate) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setBeginDate(beginDate);
        animalRequest.setEndDate(endDate);
        return animalRequest;
    }

    private AnimalRequest createAnimalRequest(boolean spam, RequestStatus status) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setSpamRequest(spam);
        animalRequest.setRequestStatus(status);
        return animalRequest;
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