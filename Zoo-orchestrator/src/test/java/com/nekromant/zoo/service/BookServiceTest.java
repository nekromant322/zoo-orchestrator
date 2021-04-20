package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.BookDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Room;
import dto.RoomDTO;
import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookServiceTest {
    @InjectMocks
    BookService bookService;

    @Autowired
    BookDAO bookDAO;

    @Mock
    AnimalRequestService animalRequestService;

    private RoomDTO fillDefaultRoomDTO(){
        return new RoomDTO(
                0L,
                AnimalType.DOG,
                RoomType.VIP,
                true,
                "",
                LocalDate.now(),
                LocalDate.now()
        );
    }

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
                            Location.MOSCOW,
                            false
                    )
        ));
        RoomDTO roomDTO = fillDefaultRoomDTO();

        Mockito.when(animalRequestService.setInProgressAnimalRequest(any())).thenThrow(new RuntimeException());
        try {
            bookService.bookAnimalRequest("228", roomDTO);
        } catch (RuntimeException e) {
            Assert.assertEquals(bookDAO.findAll().size(), 0);
        }
    }
}