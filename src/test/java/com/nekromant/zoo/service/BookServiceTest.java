package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Room;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    @InjectMocks
    BookService bookService;

    @Mock
    BookDAO bookDAO;

    @Mock
    AnimalRequestService animalRequestService;


    @Test
    public void bookAnimalRequest() {
        Optional<AnimalRequest> animalRequest = Optional.of(new AnimalRequest(
                0L,
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
        ));
        Mockito.when(animalRequestService.findById(any())).thenReturn(animalRequest);
        Mockito.when(bookDAO.save(any())).thenThrow(new RuntimeException());
        try {


            bookService.bookAnimalRequest("0",
                    new Room(
                            0L,
                            AnimalType.DOG,
                            RoomType.VIP,
                            true,
                            ""
                    ));
        } catch (RuntimeException e) {
            Assert.assertEquals(animalRequest.get().getRequestStatus(), RequestStatus.NEW);
        }
    }
}