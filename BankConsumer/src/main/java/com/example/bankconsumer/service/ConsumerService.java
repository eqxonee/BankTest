package com.example.bankconsumer.service;

import com.example.bankconsumer.dtos.ConsumerDto;
import com.example.bankconsumer.repositories.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(topics = "topic5",groupId = "group111",concurrency = "2")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
    public void listen(String message) throws JsonProcessingException {
        ConsumerDto consumerDto = objectMapper.readValue(message,ConsumerDto.class);
        System.out.println(consumerDto);


        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(consumerDto.getId())) - consumerDto.getMoneyAmount(), Math.toIntExact(consumerDto.getId()));

        //TODO Commit
    }
}
