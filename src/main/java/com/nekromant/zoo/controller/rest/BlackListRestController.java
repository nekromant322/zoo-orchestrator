package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blackList")
public class BlackListRestController {
    @Autowired
    private BlackListService blackListService;

    @PostMapping("/{id}")
    void addToBlackList(@PathVariable String id){
        blackListService.insertByAnimalRequestId(id);
    }
}
