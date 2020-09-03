package com.nekromant.zoo;

import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Authority;
import com.nekromant.zoo.model.User;
import com.nekromant.zoo.service.AnimalRequestService;
import com.nekromant.zoo.service.AuthorityService;
import com.nekromant.zoo.service.SMSCService;
import com.nekromant.zoo.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class InitData {

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    SMSCService smscSender;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AnimalRequestService animalRequestService;

    private Faker faker = new Faker(new Locale("ru"));


    public void initData() {
        initUserAndRoles();
        initAnimalRequest();
    }

    private void initUserAndRoles() {
        Authority adminAuthority = new Authority("ROLE_ADMIN");
        Authority userAuthority = new Authority("ROLE_USER");
        authorityService.insert(adminAuthority);
        authorityService.insert(userAuthority);

        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(adminAuthority);

        for (int i = 0; i < 2; i++) {
            userService.insert(new User(i + "@mail.ru", bCryptPasswordEncoder.encode(String.valueOf(i)), authorities));
        }


    }

    private void initAnimalRequest() {
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            AnimalRequest animalRequest = new AnimalRequest();
            animalRequest.setRequestStatus(RequestStatus.NEW);
            animalRequest.setAnimalType(AnimalType.values()[rnd.nextInt(AnimalType.values().length)]);
            int randomDay = rnd.nextInt(20);
            int randomMonth = rnd.nextInt(12);
            animalRequest.setBeginDate(LocalDate.of(2021, randomMonth, randomDay));
            animalRequest.setEndDate(LocalDate.of(2021, randomMonth, randomDay + 5));
            animalRequest.setRoomType(RoomType.values()[rnd.nextInt(RoomType.values().length)]);
            animalRequest.setVideoNeeded(rnd.nextBoolean());
            animalRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
            animalRequest.setEmail(faker.bothify("????##@gmail.com"));
            animalRequest.setName(faker.name().firstName());
            animalRequest.setSurname(faker.name().lastName());
            animalRequest.setAnimalName(faker.funnyName().name());
            animalRequest.setLocation(Location.values()[rnd.nextInt(Location.values().length)]);
            animalRequestService.insert(animalRequest);
        }
    }
}
