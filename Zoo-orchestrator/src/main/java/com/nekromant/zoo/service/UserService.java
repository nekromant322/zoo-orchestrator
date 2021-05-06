package com.nekromant.zoo.service;

import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.ConfirmationToken;
import com.nekromant.zoo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Value("${server.address}")
    private String address;

    @Value("${server.port}")
    private String port;

    @Value("${app.const.tokenExpiredDelayInDays}")
    private String tokenExpiredDelay;

    @Value("${app.const.secret-key-AES}")
    private String secretKey;

    public void insert(User user) {
        userDAO.save(user);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /**
     * Регистрация нового пользователя {@link User} (модальное окно с регистрацией)
     *
     * @param email    - почтовый ящик нового пользователя (он же username или login)
     * @param password - пароль от учетной записи нового пользователя
     */
    public void register(String email, String password) {
        EmailValidator validator = EmailValidator.getInstance();

        if (validator.isValid(email) && password.length() > 0) {
            if (findByEmail(email) == null) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(bCryptPasswordEncoder.encode(password));
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

    /**
     * Создание нового пользователя {@link User}
     * после подтверждения заявки {@link AnimalRequest} админом
     *
     * @param requestId - id заявки {@link AnimalRequest}
     */
    public void createUser(String requestId) {
        Optional<AnimalRequest> request = animalRequestDAO.findById(Long.valueOf(requestId));
        if (request.isPresent()) {
            AnimalRequest requestItem = request.get();
            if (findByEmail(requestItem.getEmail()) == null) {
                User user = userMapper.animalRequestToUser(requestItem);
                user.setPassword(bCryptPasswordEncoder.encode(passwordGeneratorService.generateStrongPassword()));
                user.setAuthorities(getAuthorities());
                insert(user);
                log.info("Пользователь с email {} был успешно создан при подтверждении заявки!", requestItem.getEmail());

                String secretToken = confirmationTokenService.getEncodedToken(user.getEmail(), user.getPhoneNumber());
                UriComponents url = constructUriWithQueryParameter(secretToken);

                confirmationTokenService.addToken(secretToken, user.getEmail(), LocalDate.now().plusDays(Long.parseLong(tokenExpiredDelay)));

                log.info(url.toUriString());
                emailService.sendEmail(user.getEmail(), "Подтверждение регистрации", url.toUriString());
            }
        } else {
            log.error("Заявка (AnimalRequest) с id = {} не найдена! дальнейшая работа по проверке и созданию нового клиента невозможна!", requestId);
        }
    }

    /**
     * Вход в личный кабинет пользователем (модальное окно с авторизацией)
     *
     * @param email    - email {@link User}
     * @param password - password {@link User}
     * @return - jwt token (подставляется в куки пользователя)
     */
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
                    .collect(joining(","));

            return jwtProvider.generateToken(email, authorities);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    /**
     * Смена пароля пользователя (страница с профилем юзера)
     *
     * @param email       - email {@link User}
     * @param oldPassword - password {@link User} текущий пароль
     * @param newPassword - password {@link User} новый пароль
     */
    public void changePassword(String email, String oldPassword,
                               String newPassword) {
        if (isValidCredentials(email, oldPassword, newPassword)) {
            User user = findByEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userDAO.save(user);
            log.info("Пароль для пользователя {} был успешно изменен!", email);
        } else {
            log.info("Пользователь {} не прошел валидацию данных при смене пароля!", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data!");
        }
    }

    /**
     * Подтверждение регистрации пользователем
     * После апрува заявки {@link AnimalRequest} админом отправляется письмо на почту юзера {@link User}
     * с ссылкой на страницу с подтверждением регистрации, где юзер вводит новый пароль
     *
     * @param token    - {@link ConfirmationToken} уникальное значение, которое содержит в себе мапу с мылом и номером телефона
     * @param password - {@link User} новый пароль для учетки
     * @return - jwt token (запускай работягу в лк)
     */
    public String confirmReg(String token, String password) {

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        String email = confirmationToken.getEmail();

        if (isValidCredentials(email, password)) {
            User user = findByEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userDAO.save(user);
            confirmationTokenService.deleteToken(confirmationToken);
            log.info("Пароль для пользователя {} был успешно изменен!", email);

            String authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(joining(","));
            return jwtProvider.generateToken(email, authorities);
        } else {
            log.info("Пользователь {} не прошел валидацию данных при смене пароля!", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data!");
        }
    }

    /**
     * Вспомогательный метод для проверки данных при авторизации
     * Проверяется:
     * существование юзера {@link User} в системе
     * соответствие текущего пароля
     * корректность нового пароля
     *
     * @param email       - email {@link User}
     * @param oldPassword - password {@link User} текущий пароль
     * @param newPassword - password {@link User} новый пароль
     * @return - true/false как результат проверки (true - ok, false - subzero)
     */
    public Boolean isValidCredentials(String email, String oldPassword,
                                      String newPassword) {
        if (email.isEmpty() || oldPassword.isEmpty())
            return false;

        User user = findByEmail(email);
        if (user == null)
            return false;

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            return false;

        return !oldPassword.equals(newPassword);
    }

    /**
     * Вспомогательный метод для проверки данных при авторизации
     * Проверяется:
     * существование юзера {@link User} в системе
     * корректность нового пароля
     *
     * @param email       - email {@link User}
     * @param newPassword - password {@link User} новый пароль
     * @return - true/false как результат проверки (true - ok, false - subzero)
     */
    public Boolean isValidCredentials(String email, String newPassword) {
        if (email.isEmpty() || newPassword.isEmpty())
            return false;

        User user = findByEmail(email);

        return user != null;
    }

    /**
     * Вспомогательный метод генерации ссылки для изменения пароля юзера {@link User}
     * отправляется по email юзеру
     *
     * @param token - {@link ConfirmationToken}
     * @return - url
     */
    private UriComponents constructUriWithQueryParameter(String token) {

        return UriComponentsBuilder.newInstance()
                .scheme("http").host(address).port(port)
                .path("/confirmReg").query("key={keyword}").buildAndExpand(token)
                .encode(StandardCharsets.UTF_8);
    }

    /**
     * Список ролей юзера {@link User}
     *
     * @return - List<Authority> {@link User}
     */
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
