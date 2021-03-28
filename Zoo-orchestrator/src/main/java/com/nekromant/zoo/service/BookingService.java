package com.nekromant.zoo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekromant.zoo.domain.BookParams;
import com.nekromant.zoo.exception.BookingException;
import com.nekromant.zoo.mapper.BookMapper;
import dto.BookDTO;
import dto.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;


@Service
public class BookingService {
    @Value("${booking.url}")
    private String bookingUrl;

    private static final String GET_ALL_URL = "/api/book/allBooks";
    private static final String POST_SPARE_ROOMS = "/api/book/findByRoomAndDate";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookMapper bookMapper;

    @Value(value = "${kafka.orchestratorToBookingTopic}")
    private String topic;

    @Autowired
    private RestTemplate restTemplate;

    public List<BookDTO> findAll() {
        return restTemplate.getForObject(bookingUrl + GET_ALL_URL, List.class);
    }

    public void bookAnimalRequest(String animalRequestId, RoomDTO roomDTO) {
        BookParams bookParams = bookMapper.bookParamsFromDto(animalRequestId, roomDTO);
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(bookParams));
        } catch (JsonProcessingException e) {
            throw new BookingException();
        }
    }

    public List<BookDTO> findByRoomIdAndDate(String id, LocalDate begin, LocalDate end) {
        return restTemplate.postForObject(
                bookingUrl + POST_SPARE_ROOMS,
                new BookDTO(
                        0L,
                        0L,
                        Long.parseLong(id),
                        begin,
                        end
                ),
                List.class);
    }
}
