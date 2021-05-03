package com.nekromant.zoo.service;

import com.nekromant.zoo.config.security.BCryptEncoderConfig;
import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.Authority;
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
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Constructor;
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
    private BCryptEncoderConfig bCryptEncoderConfig;

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


    @Test
    public void registerWhenAllDataIsPresented() {
        String email = "test@email.com";
        String password = "qwerty";
        Optional<Authority> authorities = Optional.of(new Authority("ROLE_USER"));

        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(authorityDAO.findByAuthority(Mockito.any())).thenReturn(authorities);
        Mockito.when(bCryptEncoderConfig.passwordEncoder()).thenReturn(Mockito.mock(BCryptPasswordEncoder.class));

        userService.register(email, password);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                bCryptEncoderConfig.passwordEncoder().encode(password), null,
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
        Mockito.when(bCryptEncoderConfig.passwordEncoder()).thenReturn(Mockito.mock(BCryptPasswordEncoder.class));

        userService.createUser(requestId);

        Mockito.verify(userDAO).save(new User(null, "test@email.com",
                bCryptEncoderConfig.passwordEncoder().encode(password), phone,
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
}