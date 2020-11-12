package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.Book;
import com.nekromant.zoo.model.Room;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {
    @InjectMocks
    RoomService roomService;

    @Mock
    RoomDAO roomDAO;

    @Mock
    BookService bookService;

    @Test
    public void findByAnimalRequest() {
        AnimalType animalType = AnimalType.DOG;
        RoomType roomType = RoomType.VIP;
        boolean video = true;

        Mockito.when(roomDAO.findAllByAnimalRequest(animalType,roomType,video)).thenReturn(
                Arrays.asList(new Room(0L,AnimalType.DOG,RoomType.VIP,true,""))
        );

        Assert.assertNotNull((roomDAO.findAllByAnimalRequest(AnimalType.DOG,RoomType.VIP,true)));
    }

    @Test
    public void findAllSpareRoom() {
        Mockito.when(bookService.findByRoomIdAndDate(any(),any(),any())).thenReturn(Collections.emptyList());
        Mockito.when(roomService.findByAnimalRequest(AnimalType.DOG,RoomType.VIP,true)).thenReturn(
                Arrays.asList(new Room(0L,
                AnimalType.DOG,
                RoomType.VIP,
                true,
                ""))
        );

        Assert.assertNotNull(roomService.findAllSpareRoom(
                AnimalType.DOG,
                RoomType.VIP,
                true, LocalDate.now(),
                LocalDate.now())
        );
    }

    @Test
    public void findAllSpareRoomWithExistBook(){
        long bookId = 0L;
        long roomId = 0L;
        long animalRequestId = 0L;
        Mockito.when(bookService.findByRoomIdAndDate("0",
                LocalDate.of(2020,4,14),
                LocalDate.of(2002,4,21))).thenReturn(
                Arrays.asList(
                        //left
                        new Book(bookId,
                                animalRequestId,
                                roomId,
                                LocalDate.of(2020,4,10),
                                LocalDate.of(2020,4,20)),
                        //right
                        new Book(bookId,
                                animalRequestId,
                                roomId,
                                LocalDate.of(2020,4,20),
                                LocalDate.of(2020,4,25)),
                        //inside
                        new Book(bookId,
                                animalRequestId,
                                roomId,
                                LocalDate.of(2020,4,15),
                                LocalDate.of(2020,4,17)),
                        //outside
                        new Book(bookId,
                                animalRequestId,
                                roomId,
                                LocalDate.of(2020,4,1),
                                LocalDate.of(2020,4,27))
                )
        );
        Mockito.when(roomService.findByAnimalRequest(AnimalType.DOG,RoomType.VIP,true)).thenReturn(
                Arrays.asList(new Room(0L,
                        AnimalType.DOG,
                        RoomType.VIP,
                        true,
                        ""))
        );

        Assert.assertEquals(
                roomService.findAllSpareRoom(
                        AnimalType.DOG,
                        RoomType.VIP,
                        true,
                        LocalDate.of(2020,4,14),
                        LocalDate.of(2002,4,21)).size(), 0);
    }
}