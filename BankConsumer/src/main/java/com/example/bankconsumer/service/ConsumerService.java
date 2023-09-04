package com.example.bankconsumer.service;

import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@EnableKafka
public class ConsumerService {

    private AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    public int findMoneyAccounts(int id) {

        return accountRepository.findMoneyAccount(id);
    }

    @KafkaListener(topics = "topic8",groupId = "group111",concurrency = "3")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
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

        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(sampleDto.getId())) - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        acknowledgment.acknowledge();

    }


}
