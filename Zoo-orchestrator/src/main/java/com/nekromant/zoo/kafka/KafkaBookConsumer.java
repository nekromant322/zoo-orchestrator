package com.nekromant.zoo.kafka;

import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/kafka.properties")
public class KafkaBookConsumer {
    @KafkaListener(topics = "${kafka.bookingToOrchestratorTopic}", groupId = "${kafka.bookingToOrchestratorGroup}")
    public void listenBookReturn(String message) {
        System.out.println("Received Message in group foo: " + message);
    }
}
