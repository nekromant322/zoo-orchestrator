package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import com.nekromant.zoo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

import static com.nekromant.zoo.enums.RoomType.*;

@RestController
@RequestMapping("/api/price")
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/actual")
    public Price getActualPrice() {
        return priceService.getActualPrice();
    }

    @PostMapping("/calc")
    public int calculateTotalPriceForRequest(@RequestBody AnimalRequest animalRequest) { return priceService.calculateTotalPrice(animalRequest); }
}
