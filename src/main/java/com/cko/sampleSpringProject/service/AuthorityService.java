package com.cko.sampleSpringProject.service;

import com.cko.sampleSpringProject.dao.AuthorityDAO;
import com.cko.sampleSpringProject.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    @Autowired
    AuthorityDAO authorityDAO;


    public void insert(Authority authority) {
        authorityDAO.save(authority);
    }
}
