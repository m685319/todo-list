package com.example.todolist.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Data
public class KafkaProperties {
    private String bootstrapServers;
    private Consumer consumer;

    @Data
    static class Consumer {
        private String groupId;
    }
}