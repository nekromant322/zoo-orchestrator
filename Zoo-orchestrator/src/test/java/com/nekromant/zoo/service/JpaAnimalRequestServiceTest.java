package com.nekromant.zoo.service;

import com.nekromant.zoo.ZooApplication;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.User;
import com.nekromant.zoo.AnimalRequestUtil;
import enums.Discount;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ZooApplication.class)
@ActiveProfiles({"dev", "test"})
@Transactional
public class JpaAnimalRequestServiceTest {
    @Autowired
    private AnimalRequestService animalRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Test
    public void bindUserAndAnimalRequestWhenUserExist() {
        User user = new User(null, "test@email.com", null, null, null, Discount.NONE, new ArrayList<>());
        AnimalRequest request = AnimalRequestUtil.make();

        userService.insert(user);
        animalRequestDAO.save(request);


        animalRequestService.bindUserAndAnimalRequest(request);

        Assert.assertEquals(userService.findByEmail("test@email.com").getAnimalRequests().get(0).getId(), Collections.singletonList(request).get(0).getId());
    }

    @Test
    public void bindUserAndAnimalRequestWhenUserNotExist() {
        AnimalRequest request = AnimalRequestUtil.make();

        Assert.assertThrows(UsernameNotFoundException.class, () -> animalRequestService.bindUserAndAnimalRequest(request));
    }
}
