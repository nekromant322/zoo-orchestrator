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

    @GetMapping("/AnimalRequestPage/onlyNew")
    public ModelAndView animalControlPage() {

        return new ModelAndView("admin/animalRequestControlPage");
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

    @GetMapping("controlPage")
    public ModelAndView controlPage() {

        return new ModelAndView("control/controlPage");
    }

    @GetMapping("/userProfilePage")
    public ModelAndView userProfilePage() {

        return new ModelAndView("userProfilePage");
    }

    @GetMapping("/confirmReg")
    public ModelAndView confirmReg() {

        return new ModelAndView("confirmReg");
    }
}
