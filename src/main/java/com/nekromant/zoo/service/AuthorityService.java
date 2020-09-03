package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityDAO authorityDAO;


    public void insert(Authority authority) {
        authorityDAO.save(authority);
    }
}
