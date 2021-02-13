package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BlackListDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.BlackList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BlackListServiceTest {

    @InjectMocks
    BlackListService blackListService;

    //create Mocks
    BlackListDAO blackListDAO = Mockito.mock(BlackListDAO.class);
    AnimalRequestService animalRequestService = Mockito.mock(AnimalRequestService.class);

    @Test
    public void insertByAnimalRequestId() {
        //behavior
        String id = "2";
        String phoneNumber = "89999999999";
        String email = "email@mail.ru";
        AnimalRequest animalRequest = new AnimalRequest();
        animalRequest.setEmail(email);
        animalRequest.setPhoneNumber(phoneNumber);
        BlackList blackList = new BlackList(0,email,phoneNumber);
        Mockito.when(animalRequestService.findById(id)).thenReturn(Optional.of(animalRequest));
        Mockito.when(blackListDAO.save(blackList)).thenReturn(blackList);
        //test
        blackListService.insertByAnimalRequestId(id);
        Mockito.verify(animalRequestService).declineAnimalRequest(any());
    }

    @Test
    public void insertByAnimalRequestIdDoesntFailWithNull() {
        //behavior
        String id = "2";
        AnimalRequest animalRequest = null;
        Mockito.when(animalRequestService.findById(id)).thenReturn(Optional.ofNullable(animalRequest));
        //test
        blackListService.insertByAnimalRequestId(id);
    }


    @Test
    public void getAll() {
        //behavior
        List<BlackList> data = new ArrayList<>();
        data.add(new BlackList(0,"email","phone"));
        Mockito.when(blackListDAO.findAll()).thenReturn(data);

        //test
        Assert.assertEquals(blackListService.getAll(),data);
    }
}