package com.qring.restaurant.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topic.queue-alarm-event}")
    private String queueAlarmEventTopic;

    @Bean
    public NewTopic queueAlarmEventTopic() {
        return new NewTopic(queueAlarmEventTopic, 1, (short) 1);
    }
}
