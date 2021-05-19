package com.nekromant.zoo.service;

import com.nekromant.zoo.client.ConfirmationZooClient;
import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.exception.InvalidChangePasswordDataException;
import com.nekromant.zoo.exception.InvalidLoginException;
import com.nekromant.zoo.exception.InvalidRegistrationDataException;
import com.nekromant.zoo.exception.UserAlreadyExistException;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.User;
import dto.ConfirmationTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class RegistrationService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationZooClient confirmationZooClient;

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
            User user = userService.findByEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userService.insert(user);
            log.info("Пароль для пользователя {} был успешно изменен!", email);
        } else {
            log.info("Пользователь {} не прошел валидацию данных при смене пароля!", email);
            throw new InvalidChangePasswordDataException("Неверное имя пользователя или пароль");
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
            log.warn("Пользователь {} не найден", email);
            throw new UsernameNotFoundException("Пользователь с email`ом " + email + " не найден!");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return jwtProvider.generateToken(email, authorities);
        }
        throw new InvalidLoginException("Пользователь не авторизован");
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
            if (userService.findByEmail(email) == null) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(bCryptPasswordEncoder.encode(password));
                user.setAuthorities(userService.generateUserRoleAuthorities());
                userService.insert(user);
                log.info("Пользователь с email {} был успешно создан с формы регистрации!", email);
            } else {
                throw new UserAlreadyExistException("Пользователь с email " + email + " уже зарегистрирован");
            }
        } else {
            throw new InvalidRegistrationDataException("Неверное имя пользователя или пароль");
        }
    }

    /**
     * Подтверждение регистрации пользователем
     * После апрува заявки {@link AnimalRequest} админом отправляется письмо на почту юзера {@link User}
     * с ссылкой на страницу с подтверждением регистрации, где юзер вводит новый пароль
     *
     * @param token    - уникальное значение, которое содержит в себе мапу с мылом и номером телефона
     * @param password - {@link User} новый пароль для учетки
     * @return - jwt token (запускай работягу в лк)
     */
    public String confirmReg(String token, String password) {

        ConfirmationTokenDTO confirmationToken = confirmationZooClient.getToken(token);

        String email = confirmationToken.getEmail();

        if (isValidCredentials(email, password)) {
            User user = userService.findByEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userService.insert(user);
            confirmationZooClient.removeToken(confirmationToken);
            log.info("Пароль для пользователя {} был успешно изменен!", email);

            String authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            return jwtProvider.generateToken(email, authorities);
        } else {
            log.info("Пользователь {} не прошел валидацию данных при смене пароля!", email);
            throw new InvalidRegistrationDataException("Неверное имя пользователя или пароль!");
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

        User user = userService.findByEmail(email);
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

        User user = userService.findByEmail(email);

        return user != null;
    }
}
