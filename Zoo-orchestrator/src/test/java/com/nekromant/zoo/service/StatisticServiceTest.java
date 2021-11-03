package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.AnimalRequestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceTest {
    @InjectMocks
    StatisticService statisticService;

    @Mock
    private AnimalRequestDAO animalRequestDAO;

    @Test
    public void getNumbersOfDoneRequestForYear() {
        int year = 2021;
        List<AnimalRequest> data = new LinkedList<>();

        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 1, 10), LocalDate.of(2021, 1, 14)));
        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 1, 10), LocalDate.of(2021, 2, 10)));
        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 4, 10), LocalDate.of(2021, 4, 20)));
        Mockito.when(animalRequestDAO.findAllByRequestStatus(Mockito.any())).thenReturn(data);

        HashMap<Month, Integer> result = statisticService.getNumbersOfDoneRequestForYear(year);

        Assert.assertEquals(result.get(Month.JANUARY), Integer.valueOf(2));
        Assert.assertEquals(result.get(Month.APRIL), Integer.valueOf(1));
    }

    @Test
    public void getMoneyYearnedForYear() {
        int year = 2021;
        List<AnimalRequest> data = new LinkedList<>();
        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 1, 10), 800));
        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 1, 10), 500));
        data.add(AnimalRequestUtil.createAnimalRequest(LocalDate.of(2021, 4, 10), 400));
        Mockito.when(animalRequestDAO.findAllByRequestStatus(Mockito.any())).thenReturn(data);

        HashMap<Month, Integer> result = statisticService.getMoneyYearnedForYear(year);

        Assert.assertEquals(result.get(Month.JANUARY), Integer.valueOf(1300));
        Assert.assertEquals(result.get(Month.APRIL), Integer.valueOf(400));
    }
}
