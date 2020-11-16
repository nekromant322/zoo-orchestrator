package com.nekromant.zoo;

import com.github.javafaker.Faker;
import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.*;
import com.nekromant.zoo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class InitData {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private SMSCService smscSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AnimalRequestService animalRequestService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PriceDAO priceDAO;

    @Autowired
    private BookDAO bookDAO;

    private Faker faker = new Faker(new Locale("ru"));


    public void initData() {
        initUserAndRoles();
        initAnimalRequest();
        initPrices();
        initRooms();
        initBooks();
        initBusyBook();
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
            int randomDay = rnd.nextInt(20) + 1;
            int randomMonth = rnd.nextInt(12) + 1;
            animalRequest.setBeginDate(LocalDate.of(2010, randomMonth, randomDay));
            animalRequest.setEndDate(LocalDate.of(2010, randomMonth, randomDay + 5));
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

        for (int i = 0; i < 5; i++) {
            AnimalRequest animalRequest = new AnimalRequest();
            animalRequest.setRequestStatus(RequestStatus.DONE);
            animalRequest.setAnimalType(AnimalType.values()[rnd.nextInt(AnimalType.values().length)]);
            int randomDay = rnd.nextInt(20) + 1;
            int randomMonth = rnd.nextInt(12) + 1;
            animalRequest.setBeginDate(LocalDate.of(2010, randomMonth, randomDay));
            animalRequest.setEndDate(LocalDate.of(2010, randomMonth, randomDay + 5));
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
        AnimalRequest animalRequestBookServiceCheck = new AnimalRequest(0L,
                RequestStatus.APPLIED,
                AnimalType.DOG,
                LocalDate.of(2010, 1, 10),
                LocalDate.of(2010, 1, 15),
                RoomType.VIP,
                true,
                faker.phoneNumber().phoneNumber(),
                faker.bothify("????##@gmail.com"),
                faker.funnyName().name(),
                faker.funnyName().name(),
                faker.name().name(),
                Location.MOSCOW
                );
        animalRequestService.insert(animalRequestBookServiceCheck);
    }

    private void initPrices() {
        Price actualPrice = new Price(0L, 100, 200, 300, 50, LocalDateTime.now());
        Price oldPrice = new Price(0L, 77, 88, 99, 55, LocalDateTime.of(2020,06,21,0,0));
        priceDAO.save(oldPrice);
        priceDAO.save(actualPrice);
    }

    private void initRooms(){
        Room room1 = new Room(0L,AnimalType.DOG,RoomType.VIP,true,"");
        Room room2 = new Room(0L,AnimalType.BIRD,RoomType.COMMON,true,"");
        Room room3 = new Room(0L,AnimalType.OTHER,RoomType.LARGE,true,"");

        roomService.insert(room1);
        roomService.insert(room2);
        roomService.insert(room3);
    }

    private void initBooks(){
        Book book1 = new Book(0L,11,1,LocalDate.of(2010,1,3),LocalDate.of(2010,1,7));
        Book book2 = new Book(0L,11,1,LocalDate.of(2010,1,21),LocalDate.of(2010,1,22));
        bookDAO.save(book1);
        bookDAO.save(book2);
    }

    private void initBusyBook(){
        Book left = new Book(0L,11,1,LocalDate.of(2010,1,3),LocalDate.of(2010,1,11));
        Book right = new Book(0L,11,1,LocalDate.of(2010,1,14),LocalDate.of(2010,1,17));
        Book inside = new Book(0L,11,1,LocalDate.of(2010,1,11),LocalDate.of(2010,1,12));
        Book outside = new Book(0L,11,1,LocalDate.of(2010,1,3),LocalDate.of(2010,1,20));

//        bookDAO.save(left);
//        bookDAO.save(right);
//        bookDAO.save(inside);
//        bookDAO.save(outside);
    }
}
