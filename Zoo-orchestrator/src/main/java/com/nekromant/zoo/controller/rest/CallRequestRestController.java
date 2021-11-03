package com.nekromant.zoo.controller.rest;


import com.nekromant.zoo.dao.CallRequestDAO;
import com.nekromant.zoo.model.CallRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/callRequest")
public class CallRequestRestController {

    @Autowired
    private CallRequestDAO callRequestDAO;

    @PostMapping
    @RequestMapping("/create")
    public void createRequest(@RequestBody CallRequest request) {
        callRequestDAO.save(request);
    }

    @PostMapping
    @RequestMapping("/delete/{id}")
    public void deleteRequest(@PathVariable long id) {
        callRequestDAO.deleteById(id);
    }

    @GetMapping
    @RequestMapping("/all")
    public List<CallRequest> getAllRequests() {
        return callRequestDAO.findAll();
    }

    @GetMapping
    @RequestMapping("/count")
    public long getCountRequests() {
        return callRequestDAO.count();
    }

}
