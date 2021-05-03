package com.nekromant.zoo.service;

import com.nekromant.zoo.config.security.BCryptEncoderConfig;
import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtProvider jwtProvider;

    public void insert(User user) {
        userDAO.save(user);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public void register(String email, String password) {
        EmailValidator validator = EmailValidator.getInstance();

        if (validator.isValid(email) && password.length() > 0) {
            if (findByEmail(email) == null) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(password));
                user.setAuthorities(getAuthorities());
                insert(user);
                log.info("Пользователь с email {} был успешно создан с формы регистрации!", email);
            } else {
                log.warn("User {} already exists", email);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists!");
            }
        } else {
            log.error("Email = {} or password = {} is invalid! Register failed!", email, password);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data!");
        }
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
                log.info("Пользователь с email {} был успешно создан при подтверждении заявки!", requestItem.getEmail());
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

    public String login(String email, String password) {
        UserDetails userDetails;
        try {
            userDetails = customUserDetailService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.warn("User {} not found", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return jwtProvider.generateToken(email, authorities);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    public Boolean isValidPassword(String email, String oldPassword,
                                   String newPassword) {
        if (email.isEmpty() || oldPassword.isEmpty())
            return false;

        User user = findByEmail(email);
        if (user == null)
            return false;

        if (!bCryptEncoderConfig.passwordEncoder().matches(oldPassword, user.getPassword()))
            return false;

        return !oldPassword.equals(newPassword);
    }

    public void changePassword(String email, String oldPassword,
                               String newPassword) {
        if (isValidPassword(email, oldPassword, newPassword)) {
            User user = findByEmail(email);
            user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(newPassword));
            userDAO.save(user);
            log.info("Пароль для пользователя {} был успешно изменен!", email);
        } else {
            log.info("Пользователь {} не прошел валидацию данных при смене пароля!", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data!");
        }
    }
}
