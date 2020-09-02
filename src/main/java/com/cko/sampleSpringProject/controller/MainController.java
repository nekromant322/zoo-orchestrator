package com.cko.sampleSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/animals")
    public ModelAndView animalsPage() {

        return new ModelAndView("animals");
    }

    @GetMapping("/main")
    public ModelAndView mainPage() {

        return new ModelAndView("main");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {

        return new ModelAndView("login");
    }
}
