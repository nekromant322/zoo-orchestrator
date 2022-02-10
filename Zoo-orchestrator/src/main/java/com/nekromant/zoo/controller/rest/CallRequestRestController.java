package com.nekromant.zoo.controller.rest;


import com.nekromant.zoo.dao.CallRequestDAO;
import com.nekromant.zoo.controller.rest.CallRequestWebSocket;
import com.nekromant.zoo.model.CallRequest;
import com.nekromant.zoo.service.CallRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/callRequest")
public class CallRequestRestController {

    @Autowired
    private CallRequestWebSocket callRequestWebSocket;

    @Autowired
    private CallRequestService callRequestService;

    @Autowired
    private CallRequestDAO callRequestDAO;

    @Value("${server.address}")
    private String address;

    @Value("${server.port}")
    private String port;

    @PostMapping
    @RequestMapping("/create")
    public void createRequest(@RequestBody CallRequest request) {
        callRequestDAO.save(request);
        callRequestWebSocket.sendMessage(String.valueOf(callRequestService.updateAndGetRequestsCount()));
    }

    @PostMapping
    @RequestMapping("/delete/{id}")
    public void deleteRequest(@PathVariable long id) {
        callRequestDAO.deleteById(id);
        callRequestWebSocket.sendMessage(String.valueOf(callRequestService.updateAndGetRequestsCount()));
    }

    @GetMapping
    @RequestMapping("/all")
    public List<CallRequest> getAllRequests() {
        return callRequestDAO.findAll();
    }

    @GetMapping
    @RequestMapping("/address")
    public String getWebSocketAddress() {
        return address + ":" + port;
    }


    @GetMapping
    @RequestMapping("/count")
    public long getCountRequests() {
        long res = callRequestService.getRequestsCount();
        callRequestWebSocket.sendMessage(String.valueOf(res));
        return res;
    }
}
