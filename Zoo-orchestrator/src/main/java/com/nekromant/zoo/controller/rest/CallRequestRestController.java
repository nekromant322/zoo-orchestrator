package com.nekromant.zoo.controller.rest;


import com.nekromant.zoo.dao.CallRequestDAO;
import com.nekromant.zoo.model.CallRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/callRequest")
public class CallRequestRestController {

    @Autowired
    private CallRequestWebSocket callRequestWebSocket;

    @Autowired
    private CallRequestDAO callRequestDAO;

    @PostMapping
    @RequestMapping("/create")
    public void createRequest(@RequestBody CallRequest request) {
        callRequestDAO.save(request);
        callRequestWebSocket.sendMessage(String.valueOf(callRequestDAO.count()));
    }

    @PostMapping
    @RequestMapping("/delete/{id}")
    public void deleteRequest(@PathVariable long id) {
        callRequestDAO.deleteById(id);
        callRequestWebSocket.sendMessage(String.valueOf(callRequestDAO.count()));
    }

    @GetMapping
    @RequestMapping("/all")
    public List<CallRequest> getAllRequests() {
        return callRequestDAO.findAll();
    }


    @GetMapping
    @RequestMapping("/count")
    public long getCountRequests() {
        long res = callRequestDAO.count();
        callRequestWebSocket.sendMessage(String.valueOf(res));
        return res;
    }
}
