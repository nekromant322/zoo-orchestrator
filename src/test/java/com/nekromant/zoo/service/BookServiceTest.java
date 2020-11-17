package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {
    @Autowired
    @InjectMocks
    BookService bookService;

    @Autowired
    BookDAO bookDAO;

    @Mock
    AnimalRequestService animalRequestService;


    @Test
    public void bookAnimalRequestTransactionTest() {
        Mockito.when(animalRequestService.findById(any())).thenReturn(
                Optional.of(
                    new AnimalRequest(
                            228L,
                            RequestStatus.NEW,
                            AnimalType.DOG,
                            LocalDate.now(),
                            LocalDate.now(),
                            RoomType.VIP,
                            true,
                            "",
                            "",
                            "",
                            "",
                            "",
                            Location.MOSCOW
                    )
        ));
        Room room = new Room(
                0L,
                AnimalType.DOG,
                RoomType.VIP,
                true,
                ""
        );

        Mockito.when(animalRequestService.setInProgressAnimalRequest(any())).thenThrow(new RuntimeException());
        try {
            bookService.bookAnimalRequest("228", room);
        } catch (RuntimeException e) {
            Assert.assertEquals(bookDAO.findAll().size(), 0);
        }
    }
}