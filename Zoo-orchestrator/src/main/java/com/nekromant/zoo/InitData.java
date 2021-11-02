package com.nekromant.zoo;

import com.github.javafaker.Faker;
import com.nekromant.zoo.dao.AnimalRequestDAO;
import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.dao.PriceDAO;
import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.model.*;
import com.nekromant.zoo.service.*;
import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AnimalRequestDAO animalRequestDAO;

    @Autowired
    private RoomDAO roomDAO;

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

        List<Authority> authorities = new ArrayList<>();
        authorities.add(adminAuthority);

        for (int i = 0; i < 2; i++) {
            userService.insert(new User(i + "@mail.ru", bCryptPasswordEncoder.encode(String.valueOf(i)), authorities));
        }

        userService.insert(new User("qwe@mail.ru", bCryptPasswordEncoder.encode("qwe"), authorities));

    }

    private void initAnimalRequest() {
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            AnimalRequest animalRequest = new AnimalRequest();
            animalRequest.setRequestStatus(RequestStatus.NEW);
            animalRequest.setAnimalType(AnimalType.values()[rnd.nextInt(AnimalType.values().length)]);
            int randomDay = rnd.nextInt(20) + 1;
            int randomMonth = rnd.nextInt(12) + 1;
            animalRequest.setBeginDate(LocalDate.of(LocalDate.now().getYear(), randomMonth, randomDay));
            animalRequest.setEndDate(LocalDate.of(LocalDate.now().getYear(), randomMonth, randomDay + 5));
            animalRequest.setRoomType(RoomType.values()[rnd.nextInt(RoomType.values().length)]);
            animalRequest.setVideoNeeded(rnd.nextBoolean());
            animalRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
            animalRequest.setEmail(faker.bothify("????##@gmail.com"));
            animalRequest.setName(faker.name().firstName());
            animalRequest.setSurname(faker.name().lastName());
            animalRequest.setAnimalName(faker.funnyName().name());
            animalRequest.setLocation(Location.values()[rnd.nextInt(Location.values().length)]);
            animalRequest.setRequestPrice((int) (Math.random() * 100));
            animalRequest.setSpamRequest(false);
            animalRequestDAO.save(animalRequest);
        }

        for (int i = 0; i < 5; i++) {
            AnimalRequest animalRequest = new AnimalRequest();
            animalRequest.setRequestStatus(RequestStatus.DONE);
            animalRequest.setAnimalType(AnimalType.values()[rnd.nextInt(AnimalType.values().length)]);
            int randomDay = rnd.nextInt(20) + 1;
            int randomMonth = rnd.nextInt(12) + 1;
            animalRequest.setBeginDate(LocalDate.of(LocalDate.now().getYear(), randomMonth, randomDay));
            animalRequest.setEndDate(LocalDate.of(LocalDate.now().getYear(), randomMonth, randomDay + 5));
            animalRequest.setRoomType(RoomType.values()[rnd.nextInt(RoomType.values().length)]);
            animalRequest.setVideoNeeded(rnd.nextBoolean());
            animalRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
            animalRequest.setEmail(faker.bothify("????##@gmail.com"));
            animalRequest.setName(faker.name().firstName());
            animalRequest.setSurname(faker.name().lastName());
            animalRequest.setAnimalName(faker.funnyName().name());
            animalRequest.setLocation(Location.values()[rnd.nextInt(Location.values().length)]);
            animalRequest.setRequestPrice((int) (Math.random() * 100));
            animalRequest.setSpamRequest(false);
            animalRequestDAO.save(animalRequest);
        }
        AnimalRequest animalRequestBookServiceCheck = new AnimalRequest(0L,
                RequestStatus.APPLIED,
                AnimalType.DOG,
                LocalDate.of(LocalDate.now().getYear(), 1, 10),
                LocalDate.of(LocalDate.now().getYear(), 1, 15),
                RoomType.VIP,
                true,
                faker.phoneNumber().phoneNumber(),
                faker.bothify("????##@gmail.com"),
                faker.funnyName().name(),
                faker.funnyName().name(),
                faker.name().name(),
                (int) (Math.random() * 100),
                Location.MOSCOW,
                false);
        animalRequestDAO.save(animalRequestBookServiceCheck);
    }

    private void initPrices() {
        Price actualPrice = new Price(0L, 100, 200, 300, 500, 200, 75, 100, 150, 150, 50, LocalDateTime.now(), 0.9, 0.85);
        Price oldPrice = new Price(0L, 77, 88, 99, 55, 66, 44, 22, 88, 111, 25, LocalDateTime.of(2020, 06, 21, 0, 0), 0.95, 0.9);
        priceDAO.save(oldPrice);
        priceDAO.save(actualPrice);
    }

    private void initRooms() {
        Room room1 = new Room(0L, AnimalType.DOG, RoomType.COMMON, true, "");
        Room room2 = new Room(0L, AnimalType.DOG, RoomType.LARGE, true, "");
        Room room3 = new Room(0L, AnimalType.DOG, RoomType.VIP, true, "");
        Room room4 = new Room(0L, AnimalType.BIRD, RoomType.COMMON, true, "");
        Room room5 = new Room(0L, AnimalType.BIRD, RoomType.LARGE, true, "");
        Room room6 = new Room(0L, AnimalType.BIRD, RoomType.VIP, true, "");
        Room room7 = new Room(0L, AnimalType.OTHER, RoomType.COMMON, true, "");
        Room room8 = new Room(0L, AnimalType.OTHER, RoomType.LARGE, true, "");
        Room room9 = new Room(0L, AnimalType.OTHER, RoomType.VIP, true, "");
        Room room10 = new Room(0L, AnimalType.OTHER, RoomType.COMMON, true, "");
        Room room11 = new Room(0L, AnimalType.DOG, RoomType.COMMON, false, "");
        Room room12 = new Room(0L, AnimalType.DOG, RoomType.LARGE, false, "");
        Room room13 = new Room(0L, AnimalType.DOG, RoomType.VIP, false, "");
        Room room14 = new Room(0L, AnimalType.BIRD, RoomType.COMMON, false, "");
        Room room15 = new Room(0L, AnimalType.BIRD, RoomType.LARGE, false, "");
        Room room16 = new Room(0L, AnimalType.BIRD, RoomType.VIP, false, "");
        Room room17 = new Room(0L, AnimalType.OTHER, RoomType.COMMON, false, "");
        Room room18 = new Room(0L, AnimalType.OTHER, RoomType.LARGE, false, "");
        Room room19 = new Room(0L, AnimalType.OTHER, RoomType.VIP, false, "");

        roomDAO.save(room1);
        roomDAO.save(room2);
        roomDAO.save(room3);
        roomDAO.save(room4);
        roomDAO.save(room5);
        roomDAO.save(room6);
        roomDAO.save(room7);
        roomDAO.save(room8);
        roomDAO.save(room9);
        roomDAO.save(room10);
        roomDAO.save(room11);
        roomDAO.save(room12);
        roomDAO.save(room13);
        roomDAO.save(room14);
        roomDAO.save(room15);
        roomDAO.save(room16);
        roomDAO.save(room17);
        roomDAO.save(room18);
        roomDAO.save(room19);
    }

    private void initBooks() {
        Book book1 = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 3), LocalDate.of(LocalDate.now().getYear(), 1, 7));
        Book book2 = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 21), LocalDate.of(LocalDate.now().getYear(), 1, 22));
        bookDAO.save(book1);
        bookDAO.save(book2);
    }

    private void initBusyBook() {
        Book left = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 3), LocalDate.of(LocalDate.now().getYear(), 1, 11));
        Book right = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 14), LocalDate.of(LocalDate.now().getYear(), 1, 17));
        Book inside = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 11), LocalDate.of(LocalDate.now().getYear(), 1, 12));
        Book outside = new Book(0L, 11, 1, LocalDate.of(LocalDate.now().getYear(), 1, 3), LocalDate.of(LocalDate.now().getYear(), 1, 20));

//        bookDAO.save(left);
//        bookDAO.save(right);
//        bookDAO.save(inside);
//        bookDAO.save(outside);
    }
}
