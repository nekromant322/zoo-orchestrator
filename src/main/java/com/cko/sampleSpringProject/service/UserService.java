package com.cko.sampleSpringProject.service;

import com.cko.sampleSpringProject.dao.UserDAO;
import com.cko.sampleSpringProject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with name " + email + " not found");
        }
        return user;
    }

    public void insert(User user) {
        userDAO.save(user);
    }
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
