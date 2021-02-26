package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Price;
import com.nekromant.zoo.service.PriceService;
import dto.AnimalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricePage")
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/actual")
    public Price getActualPrice() {
        return priceService.getActualPrice();
    }

    @PostMapping("/calculate")
    public int calculateTotalPriceForRequest(@RequestBody AnimalRequestDTO animalRequestDTO) {
        return priceService.calculateTotalPrice(animalRequestDTO);
    }

    @PostMapping("/new")
    public void newPrice(@RequestBody Price price) {
        priceService.insert(price);
    }

}
