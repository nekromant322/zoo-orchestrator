package com.nekromant.zoo.controller;

import com.nekromant.zoo.service.PhotoHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhotoGalleryController {

    @Autowired
    private PhotoHolder photoHolder;

    @GetMapping("/photoGallery/photos")
    public List<String> getUrlPhotos() {
        return photoHolder.getUrlPhotos();
    }
}
