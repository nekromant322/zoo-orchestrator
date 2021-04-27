package com.nekromant.zoo.service;

import com.nekromant.zoo.config.BCryptEncoderConfig;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private BCryptEncoderConfig bCryptEncoderConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

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

    public void createUser(String requestId) {
        Optional<AnimalRequest> request = animalRequestDAO.findById(Long.valueOf(requestId));
        if (request.isPresent()) {
            AnimalRequest requestItem = request.get();
            if (findByEmail(requestItem.getEmail()) == null) {
                User user = userMapper.animalRequestToUser(requestItem);
                user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(passwordGeneratorService.generateStrongPassword()));
                user.setAuthorities(getAuthorities());
                insert(user);
                log.info("Пользователь с email {} был успешно создан!", requestItem.getEmail());
            }
        } else {
            log.error("Заявка (AnimalRequest) с id = {} не найдена! дальнейшая работа по проверке и созданию нового клиента невозможна!", requestId);
        }
    }

    private List<Authority> getAuthorities() {
        Optional<Authority> authority = authorityDAO.findByAuthority("ROLE_USER");
        if (authority.isPresent()) {
            List<Authority> list = new ArrayList<>();
            list.add(authority.get());
            return list;
        }
        return null;
    }
}
