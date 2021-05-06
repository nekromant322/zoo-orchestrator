package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.PhotoHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PhotoGalleryController {

    @Autowired
    private PhotoHolder photoHolder;

    @GetMapping("/photoGalleryPage")
    public ModelAndView photoGalleryPage() {
        ModelAndView modelAndView = new ModelAndView("photoGalleryPage");
        modelAndView.addObject("urls", photoHolder.getUrlPhotos());
        return modelAndView;
    }

}
