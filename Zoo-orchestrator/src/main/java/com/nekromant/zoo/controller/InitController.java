package com.nekromant.zoo.controller;

import com.nekromant.zoo.InitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/init")
public class InitController {
    @Autowired
    private InitData initData;

    private boolean init = false;

    @GetMapping
    public RedirectView init(){
        if(!init) {
            initData.initData();
            init = true;
        }
        return new RedirectView("/AnimalRequestPage/onlyNew");
    }
}
