package com.example.bankconsumer.service;

import com.example.bankconsumer.configs.KafkaConsumerConfig;
import com.example.bankconsumer.dtos.ConsumerDto;
import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
@AllArgsConstructor
public class ConsumerService {

    private AccountRepository accountRepository;
    private ModelMapper modelMapper;

    //private KafkaConsumer kafkaConsumer;

    public int findMoneyAccounts(int id) {

        return accountRepository.findMoneyAccount(id);
    }

    @KafkaListener(topics = "topic3",groupId = "group111")
    public void listen(@Payload ConsumerDto consumerDto){
        System.out.println(consumerDto);
        Gson gson = new Gson();

        Account account = gson.fromJson(String.valueOf(consumerDto),Account.class);

        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(account.getId())) - account.getMoneyAmount(), Math.toIntExact(account.getId()));
    }

//    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
//    public void updateMoney() {
//
//
//        kafkaConsumer.subscribe(Collections.singleton("topic3"));
//        Gson gson = new Gson();
//
//        while (true) {
//            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
//
//            for (ConsumerRecord<String, String> record : records) {
//                Account account = gson.fromJson(record.value(), Account.class);
//
//                System.out.println(account.getId());
//                System.out.println(account.getMoneyAmount());
//
//
//            }
//        }
//    }
}
