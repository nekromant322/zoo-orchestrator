package com.nekromant.zoo.controller;

import com.nekromant.zoo.config.aspect.Metric;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.nekromant.zoo.constant.MetricContants.CONFIRM_REGISTRATION_PAGE_METRIC;
import static com.nekromant.zoo.constant.MetricContants.MAIN_PAGE_METRIC;

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

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("mainPage");
    }

    @Metric(MAIN_PAGE_METRIC)
    @GetMapping("/mainPage")
    public ModelAndView mainPage() {
        return new ModelAndView("mainPage");
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

    @Metric(CONFIRM_REGISTRATION_PAGE_METRIC)
    @GetMapping("/confirmReg")
    public ModelAndView confirmReg() {
        return new ModelAndView("confirmReg");
    }

    @GetMapping("/photoGalleryPage")
    public ModelAndView photoGalleryPage() {
        return new ModelAndView("photoGalleryPage");
    }

    @GetMapping("/mailingPage")
    public ModelAndView mailingPage() {
        return new ModelAndView("mailingPage");
    }

    @GetMapping("/bookingPage")
    public ModelAndView bookingPage() {
        return new ModelAndView("bookingPage");
    }
}
