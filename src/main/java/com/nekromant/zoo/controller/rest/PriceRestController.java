package com.nekromant.zoo.controller.rest;

import com.cko.zoo.dto.AnimalRequestDTO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import com.nekromant.zoo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public int calculateTotalPriceForRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        return priceService.calculateTotalPrice(animalRequestDTO);
    }
}
