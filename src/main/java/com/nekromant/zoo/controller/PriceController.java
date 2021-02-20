package com.nekromant.zoo.controller;

import com.nekromant.zoo.model.Price;
import com.nekromant.zoo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/pricePage")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/edit")
    public ModelAndView editPricesPage() {
        ModelAndView modelAndView = new ModelAndView("pricePage");
        modelAndView.addObject("actualPrice", priceService.getActualPrice());
        return modelAndView;
    }

}
