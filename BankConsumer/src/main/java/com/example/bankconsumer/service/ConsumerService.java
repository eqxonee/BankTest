package com.example.bankconsumer.service;

import com.example.bankconsumer.dtos.ConsumerDto;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.sampleDto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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

    @KafkaListener(topics = "topic5",groupId = "group111",concurrency = "3")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
    public void listen(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        SampleDto sampleDto = objectMapper.readValue(message,SampleDto.class);
        System.out.println(sampleDto);


        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(sampleDto.getId())) - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        acknowledgment.acknowledge();

    }
}
