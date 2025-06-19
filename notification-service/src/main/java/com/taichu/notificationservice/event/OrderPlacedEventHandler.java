package com.taichu.notificationservice.event;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventHandler {
   
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification (OrderPlacedEvent orderPlacedEvent) {
        // send out an email
        log.info("Received notification for Order - ()", orderPlacedEvent.getOrderNumber());
    }
}