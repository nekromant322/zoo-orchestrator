package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.CallRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CallRequestService {

    @Autowired
    private CallRequestDAO callRequestDAO;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(cacheNames = "requestsCount")
    public long getRequestsCount() {
        return callRequestDAO.count();
    }

    public long updateAndGetRequestsCount() {
        cacheManager.getCache("requestsCount").clear();
        return callRequestDAO.count();
    }
}
