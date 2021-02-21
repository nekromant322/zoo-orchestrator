package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.AnimalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.HashMap;

@RestController
@RequestMapping("/api/chartPage")
public class ChartRestController {

    @Autowired
    private AnimalRequestService animalRequestService;

    @GetMapping("/requestNumber")
    public HashMap<Month, Integer> getNumbersOfDoneRequestForYear(@RequestParam("year") int year) {
        return animalRequestService.getNumbersOfDoneRequestForYear(year);
    }

    @GetMapping("/moneyEarned")
    public HashMap<Month, Integer> getMoneyYearnedForYear(@RequestParam("year") int year) {
        return animalRequestService.getMoneyYearnedForYear(year);
    }
}
