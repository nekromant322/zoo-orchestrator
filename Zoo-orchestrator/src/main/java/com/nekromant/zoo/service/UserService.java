package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.exception.AnimalRequestNotFoundException;
import com.nekromant.zoo.exception.AuthoritiesNotFoundException;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private QueryConstructorService queryConstructorService;

    @Autowired
    private AnimalRequestService animalRequestService;

    public void insert(User user) {
        userDAO.save(user);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /**
     * Создание нового пользователя {@link User}
     * после подтверждения заявки {@link AnimalRequest} админом
     *
     * @param requestId - id заявки {@link AnimalRequest}
     */
    public void createUser(String requestId) {
        Optional<AnimalRequest> request = animalRequestService.findById(requestId);
        if (request.isPresent()) {
            AnimalRequest requestItem = request.get();
            if (findByEmail(requestItem.getEmail()) == null) {
                User user = userMapper.animalRequestToUser(requestItem);
                user.setPassword(bCryptPasswordEncoder.encode(passwordGeneratorService.generateStrongPassword()));
                user.setAuthorities(generateUserRoleAuthorities());
                user.setAnimalRequests(Collections.singletonList(requestItem));
                insert(user);
                log.info("Пользователь с email {} был успешно создан при подтверждении заявки!", requestItem.getEmail());

                String secretToken = confirmationTokenService.getEncodedToken(user.getEmail(), user.getPhoneNumber());
                UriComponents url = queryConstructorService.buildConfirmationUrlWithToken(secretToken);

                confirmationTokenService.addToken(secretToken, user.getEmail());

                log.info(url.toUriString());
                emailService.sendEmail(user.getEmail(), "Подтверждение регистрации", url.toUriString());
            } else {
                animalRequestService.bindUserAndAnimalRequest(requestItem);
            }
        } else {
            throw new AnimalRequestNotFoundException(requestId);
        }
    }

    /**
     * Список ролей юзера {@link User}
     *
     * @return - List<Authority> {@link User}
     */
    protected List<Authority> generateUserRoleAuthorities() {
        Optional<Authority> authority = authorityDAO.findByAuthority("ROLE_USER");
        if (authority.isPresent()) {
            List<Authority> list = new ArrayList<>();
            list.add(authority.get());
            return list;
        }

        throw new AuthoritiesNotFoundException("Пользовательские права не найдены! Роль 'ROLE_USER' отсутствует!");
    }

}
