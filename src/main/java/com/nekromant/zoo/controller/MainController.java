package com.nekromant.zoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/newAnimalRequest")
    public ModelAndView animalsPage() {

        return new ModelAndView("newAnimalRequest");
    }

    @GetMapping("/main")
    public ModelAndView mainPage() {

        return new ModelAndView("main");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {

        return new ModelAndView("login");
    }

    @GetMapping("/video")
    public ModelAndView testVideoPage() {

        return new ModelAndView("video");
    }

    @GetMapping("/chart")
    public ModelAndView testChartPage() {

        return new ModelAndView("chart");
    }
}
