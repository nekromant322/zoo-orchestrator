package com.nekromant.zoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/animalRequestPage")
    public ModelAndView animalsPage() {

        return new ModelAndView("animalRequestPage");
    }

    @GetMapping("/mainPage")
    public ModelAndView mainPage() {

        return new ModelAndView("mainPage");
    }

    @GetMapping("/loginPage")
    public ModelAndView loginPage() {

        return new ModelAndView("loginPage");
    }

    @GetMapping("/videoPage")
    public ModelAndView testVideoPage() {

        return new ModelAndView("videoPage");
    }

    @GetMapping("/chartPage")
    public ModelAndView testChartPage() {

        return new ModelAndView("chartPage");
    }

    @GetMapping("/pricePage")
    public ModelAndView testPricePage() {

        return new ModelAndView("admin/pricePage");
    }
}
