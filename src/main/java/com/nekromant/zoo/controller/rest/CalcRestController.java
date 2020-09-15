package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

import static com.nekromant.zoo.enums.RoomType.*;


@RestController
@RequestMapping("/api")
public class CalcRestController {

    @Autowired
    PriceService priceService;

    @PostMapping("/calc")
    public int getPrice(@RequestBody AnimalRequest animalRequest) {
        LocalDate begin = animalRequest.getBeginDate();
        LocalDate end = animalRequest.getEndDate();
        Period period = Period.between(begin, end);
        int difference = period.getDays();
        int price = 0;
        if (animalRequest.getRoomType() == COMMON) {
            price = difference * priceService.getActualPrice().getCommonRoomPrice();
        }
        if (animalRequest.getRoomType() == LARGE) {
            price = difference * priceService.getActualPrice().getLargeRoomPrice();
        }
        if (animalRequest.getRoomType() == VIP) {
            price = difference * priceService.getActualPrice().getVipRoomPrice();
        }
        if (animalRequest.getVideoNeeded() == true) {
            price += priceService.getActualPrice().getVideoPrice();
        }
        return price;
    }
}