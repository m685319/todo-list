package com.example.todolist.service;

import com.example.todolist.dto.TaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaTaskListener {

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(TaskDTO message) {
        log.info("Received message: {}", message);
    }
}
