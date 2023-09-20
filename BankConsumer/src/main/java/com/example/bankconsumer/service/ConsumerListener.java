package com.example.bankconsumer.service;

import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
@EnableKafka
public class ConsumerListener {

    private final ObjectMapper objectMapper;
    private ConsumerKafkaService consumerKafkaService;

    @KafkaListener(topics = "topic13",groupId = "group111",concurrency = "3")
    public void listen(String message, Acknowledgment acknowledgment,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws JsonProcessingException {

        SampleDto sampleDto = objectMapper.readValue(message,SampleDto.class);
        System.out.println(sampleDto);
        System.out.println(key);
        System.out.println(partition);
        System.out.println(topic);
        System.out.println(ts);

        consumerKafkaService.accountUpdate(sampleDto);

        acknowledgment.acknowledge();

    }


}
