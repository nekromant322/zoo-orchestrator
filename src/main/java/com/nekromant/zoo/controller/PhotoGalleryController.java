package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.PhotoHolder;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PhotoGalleryController {

    @Autowired
    private PhotoHolder photoHolder;

    @GetMapping("/photogallery")
    public ModelAndView testPhotoGallery() throws ClientException, ApiException {
        ModelAndView modelAndView = new ModelAndView("photoGallery");
        modelAndView.addObject("urls",photoHolder.getUrlPhotos());
        return modelAndView;
    }

}
