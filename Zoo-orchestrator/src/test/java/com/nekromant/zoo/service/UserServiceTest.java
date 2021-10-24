package com.nekromant.zoo.service;

import com.nekromant.zoo.client.ConfirmationZooClient;
import com.nekromant.zoo.client.NotificationZooClient;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.AuthorityDAO;
import com.nekromant.zoo.dao.UserDAO;
import com.nekromant.zoo.mapper.UserMapper;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import com.nekromant.zoo.service.util.AnimalRequestUtil;
import enums.Discount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    private AnimalRequestService animalRequestService;

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
    private ConfirmationZooClient confirmationZooClient;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private NotificationZooClient notificationZooClient;

    @Mock
    private QueryConstructorService queryConstructorService;

    @Test
    public void createUser() {
        String requestId = "1";
        String password = "qwerty";
        String phone = "+7(999)-(999)-(99)-(99)";
        Optional<Authority> authorities = Optional.of(new Authority("ROLE_USER"));
        AnimalRequest request = AnimalRequestUtil.make();

        Mockito.when(animalRequestService.findById(Mockito.any())).thenReturn(Optional.of(AnimalRequestUtil.make()));
        Mockito.when(userDAO.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(authorityDAO.findByAuthority(Mockito.any())).thenReturn(authorities);
        Mockito.when(passwordGeneratorService.generateStrongPassword()).thenReturn(password);
        Mockito.when(confirmationZooClient.getEncodedToken(Mockito.any(), Mockito.any())).thenReturn("qwe");
        Mockito.when(queryConstructorService.buildConfirmationUrlWithToken(Mockito.any())).thenReturn(UriComponentsBuilder.newInstance().build());

        userService.createUser(requestId);

        Mockito.verify(userDAO).save(new User(null, LocalDate.now(), "test@email.com",
                bCryptPasswordEncoder.encode(password), phone,
                Collections.singletonList(authorities.get()), Discount.NONE, Collections.singletonList(request)));
    }
}