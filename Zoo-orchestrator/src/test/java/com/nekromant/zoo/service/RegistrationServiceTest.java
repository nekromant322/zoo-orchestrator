package com.nekromant.zoo.service;

import com.nekromant.zoo.client.ConfirmationZooClient;
import com.nekromant.zoo.config.security.JwtProvider;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.exception.InvalidRegistrationDataException;
import com.nekromant.zoo.exception.UserAlreadyExistException;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import dto.ConfirmationTokenDTO;
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

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {
    @InjectMocks
    RegistrationService registrationService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserMapper userMapper;

    @Mock
    private AuthorityDAO authorityDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CustomUserDetailService customUserDetailService;

    @Mock
    private ConfirmationZooClient confirmationZooClient;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;


    @Test
    public void registerWhenAllDataIsPresented() {
        String email = "test@email.com";
        String password = "qwerty";

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(userService.generateUserRoleAuthorities()).thenReturn(null);

        registrationService.register(email, password);

        Mockito.verify(userService).insert(new User(null, "test@email.com",
                bCryptPasswordEncoder.encode(password), null,
                null, Discount.NONE, new ArrayList<>()));
    }

    @Test
    public void registerWhenUserExists() {
        String email = "test@email.com";
        String password = "qwerty";

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(new User());

        Assert.assertThrows(UserAlreadyExistException.class, () -> registrationService.register(email, password));
    }

    @Test
    public void registerWhenAnyDataIsEmptyOrInvalid() {
        Assert.assertThrows(InvalidRegistrationDataException.class, () -> registrationService.register("", "qwe"));
        Assert.assertThrows(InvalidRegistrationDataException.class, () -> registrationService.register("test", "qwe"));
        Assert.assertThrows(InvalidRegistrationDataException.class, () -> registrationService.register("test@", "qwe"));
        Assert.assertThrows(InvalidRegistrationDataException.class, () -> registrationService.register("test@gmail.com", ""));
        Assert.assertThrows(InvalidRegistrationDataException.class, () -> registrationService.register("", ""));
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

        String res = registrationService.login(email, password);

        Assert.assertEquals(res, "key");
    }

    @Test
    public void loginUserNotExists() {
        String email = "test@email.com";
        String password = "qwerty";

        Mockito.when(customUserDetailService.loadUserByUsername(Mockito.any())).thenThrow(new UsernameNotFoundException(""));

        Assert.assertThrows(UsernameNotFoundException.class, () -> registrationService.login(email, password));
    }

    @Test
    public void isValidPasswordWithCorrectData() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE, null);

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(password, password)).thenReturn(true);

        Boolean res = registrationService.isValidCredentials(email, password, newPassword);

        Assert.assertEquals(res, true);

        res = registrationService.isValidCredentials(email, newPassword);

        Assert.assertEquals(res, true);
    }

    @Test
    public void isValidPasswordWithInCorrectData() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE, null);

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(password, password)).thenReturn(false);

        //
        Boolean res = registrationService.isValidCredentials(email, "qwerty1", newPassword);
        Assert.assertEquals(res, false);

        res = registrationService.isValidCredentials(email, "", newPassword);
        Assert.assertEquals(res, false);

        res = registrationService.isValidCredentials(email, password, "");
        Assert.assertEquals(res, false);

        res = registrationService.isValidCredentials(email, password, newPassword);
        Assert.assertEquals(res, false);

        res = registrationService.isValidCredentials(email, password, password);
        Assert.assertEquals(res, false);

        //
        res = registrationService.isValidCredentials(email, "");
        Assert.assertEquals(res, false);

        res = registrationService.isValidCredentials("", newPassword);
        Assert.assertEquals(res, false);
    }

    @Test
    public void changePassword() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        User user = new User(null, email, password, "", null, Discount.NONE, null);

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn(newPassword);

        registrationService.changePassword(email, password, newPassword);

        Mockito.verify(userService).insert(new User(null, "test@email.com",
                bCryptPasswordEncoder.encode("123"), "", null, Discount.NONE, null));
    }

    @Test
    public void confirmReg() {
        String email = "test@email.com";
        String password = "qwerty";
        String newPassword = "123";
        Authority authority = new Authority("ROLE_USER");
        List<Authority> list = new ArrayList<>();
        list.add(authority);

        User user = new User(null, email, password, "", list, Discount.NONE, null);
        ConfirmationTokenDTO confirmationToken = new ConfirmationTokenDTO(1, "qwe", email, LocalDate.now());

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn(password);
        Mockito.when(confirmationZooClient.getToken(Mockito.any())).thenReturn(confirmationToken);

        registrationService.confirmReg(email, newPassword);

        Mockito.verify(userService).insert(new User(null, "test@email.com",
                password, "", list, Discount.NONE, null));
    }
}
