package com.nekromant.zoo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekromant.zoo.domain.BookParams;
import dto.BookDTO;
import dto.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;


@Service
@PropertySource(value = "classpath:config/booking.properties")
public class BookService {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${kafka.orchestratorToBookingTopic}")
    private String topic;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${booking.getAllUrl}")
    private String getAllUrl;

    @Value("${booking.postSpareUrl}")
    private String postSpareUrl;

    public List findAll() {
        return restTemplate.getForObject(getAllUrl, List.class);
    }

    public void bookAnimalRequest(String animalRequestId, RoomDTO roomDTO) {
        BookParams bookParams = new BookParams(
                Long.parseLong(animalRequestId),
                roomDTO.getId(),
                roomDTO.getAnimalType(),
                roomDTO.getRoomType(),
                roomDTO.getVideoSupported(),
                roomDTO.getBegin().toString(),
                roomDTO.getEnd().toString()
        );
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(bookParams));
        } catch (JsonProcessingException e){
            System.out.println(e);
        }
    }

    public List<BookDTO> findByRoomIdAndDate(String id, LocalDate begin, LocalDate end){
        return restTemplate.postForObject(
                postSpareUrl,
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
