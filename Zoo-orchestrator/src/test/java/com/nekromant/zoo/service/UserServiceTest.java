package com.nekromant.zoo.service;

import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.ConfirmationTokenDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.ConfirmationToken;
import com.nekromant.zoo.model.User;
import com.nekromant.zoo.service.util.AnimalRequestUtil;
import enums.Discount;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    private AnimalRequestDAO animalRequestDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserMapper userMapper;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private AuthorityDAO authorityDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CustomUserDetailService customUserDetailService;

    @Mock
    private ConfirmationTokenDAO confirmationTokenDAO;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailService emailService;


    @Test
    public void registerWhenAllDataIsPresented() {
        String email = "test@email.com";
        String password = "qwerty";
        Optional<Authority> authorities = Optional.of(new Authority("ROLE_USER"));

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(authorityDAO.findByAuthority(Mockito.any())).thenReturn(authorities);

        userService.register(email, password);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                bCryptPasswordEncoder.encode(password), null,
                Collections.singletonList(authorities.get()), Discount.NONE));
    }

    @Test
    public void registerWhenUserExists() {
        String email = "test@email.com";
        String password = "qwerty";

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(new User());

        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register(email, password);
        });
    }

    @Test
    public void registerWhenAnyDataIsEmptyOrInvalid() {
        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register("", "qwe");
        });
        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register("test", "qwe");
        });
        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register("test@", "qwe");
        });
        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register("test@gmail.com", "");
        });
        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.register("", "");
        });
    }

    @Test
    public void createUser() {
        String requestId = "1";
        String password = "qwerty";
        String phone = "+7(999)-(999)-(99)-(99)";
        Optional<Authority> authorities = Optional.of(new Authority("ROLE_USER"));

        Mockito.when(animalRequestDAO.findById(Mockito.any())).thenReturn(Optional.of(AnimalRequestUtil.make()));
        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(authorityDAO.findByAuthority(Mockito.any())).thenReturn(authorities);
        Mockito.when(passwordGeneratorService.generateStrongPassword()).thenReturn(password);
        Mockito.when(confirmationTokenService.getEncodedToken(Mockito.any(), Mockito.any())).thenReturn("qwe");
        ReflectionTestUtils.setField(userService, "tokenExpiredDelay", "3");
        // Mockito.when(confirmationTokenService.addToken(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn();

        userService.createUser(requestId);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                bCryptPasswordEncoder.encode(password), phone,
                Collections.singletonList(authorities.get()), Discount.NONE));
    }

    @SneakyThrows
    @Test
    public void loginUserExists() {
        String email = "test@email.com";
        String password = "qwerty";
        Authority authority = new Authority("ROLE_USER");
        List<Authority> list = new ArrayList<>();
        list.add(authority);
        User user = new User(email, password, list);
        Class<?> clazz = Class.forName("com.nekromant.zoo.service.CustomUserDetailService$CustomUserDetails");
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object userDetails = constructor.newInstance(user);

        Mockito.when(customUserDetailService.loadUserByUsername(Mockito.any())).thenReturn((UserDetails) userDetails);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(jwtProvider.generateToken(Mockito.any(), Mockito.any())).thenReturn("key");

        String res = userService.login(email, password);

        Assert.assertEquals(res, "key");
    }

    @Test
    public void loginUserNotExists() {
        String email = "test@email.com";
        String password = "qwerty";

        Mockito.when(customUserDetailService.loadUserByUsername(Mockito.any())).thenThrow(new UsernameNotFoundException(""));

        Assert.assertThrows(ResponseStatusException.class, () -> {
            userService.login(email, password);
        });
    }

    @Test
    public void isValidPasswordWithCorrectData() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE);

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(password, password)).thenReturn(true);

        Boolean res = userService.isValidCredentials(email, password, newPassword);

        Assert.assertEquals(res, true);

        res = userService.isValidCredentials(email, newPassword);

        Assert.assertEquals(res, true);
    }

    @Test
    public void isValidPasswordWithInCorrectData() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE);

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(password, password)).thenReturn(false);

        //
        Boolean res = userService.isValidCredentials(email, "qwerty1", newPassword);
        Assert.assertEquals(res, false);

        res = userService.isValidCredentials(email, "", newPassword);
        Assert.assertEquals(res, false);

        res = userService.isValidCredentials(email, password, "");
        Assert.assertEquals(res, false);

        res = userService.isValidCredentials(email, password, newPassword);
        Assert.assertEquals(res, false);

        res = userService.isValidCredentials(email, password, password);
        Assert.assertEquals(res, false);

        //
        res = userService.isValidCredentials(email, "");
        Assert.assertEquals(res, false);

        res = userService.isValidCredentials("", newPassword);
        Assert.assertEquals(res, false);
    }

    @Test
    public void changePassword() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE);

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn(newPassword);

        userService.changePassword(email, password, newPassword);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                bCryptPasswordEncoder.encode("123"), "", null, Discount.NONE));
    }

    @Test
    public void confirmReg() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        Authority authority = new Authority("ROLE_USER");
        List<Authority> list = new ArrayList<>();
        list.add(authority);
        User user = new User(null, email, password, "", list, Discount.NONE);
        ConfirmationToken confirmationToken = new ConfirmationToken("qwe", email, LocalDate.now());

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn(password);
        Mockito.when(confirmationTokenService.getToken(Mockito.any())).thenReturn(confirmationToken);

        userService.confirmReg(email, newPassword);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                password, "", list, Discount.NONE));
    }
}