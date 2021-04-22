package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.HashMap;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/requestNumber")
    public HashMap<Month, Integer> getNumbersOfDoneRequestForYear(@RequestParam("year") int year) {
        return statisticService.getNumbersOfDoneRequestForYear(year);
    }

    @GetMapping("/moneyEarned")
    public HashMap<Month, Integer> getMoneyYearnedForYear(@RequestParam("year") int year) {
        return statisticService.getMoneyYearnedForYear(year);
    }
}
