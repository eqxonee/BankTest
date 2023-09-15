package com.example.bankproducer.service;

import com.example.bankproducer.controllers.AccountController;
import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"})
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AccountController accountController;

    @Autowired
    private MockMvc mockMvc;


    BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "topic11", groupId = "group111", concurrency = "3")
    public void listen(ConsumerRecord consumerRecord) throws JsonProcessingException {
        consumerRecords.add(consumerRecord);
    }

    @Test
    void sendMessage() throws Exception {
        //AccountService accountService = new AccountService(null,null,null,null);

        String URL = "http://localhost:8085/account/kafka-update";
        assertThat(accountController).isNotNull();

        SampleDto sampleDto = new SampleDto(3L, 1000);

        accountService.sendMessage(sampleDto);

        mockMvc.perform(post(URL)
                        .content(objectMapper.writeValueAsString(sampleDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);
        assert received != null;

        SampleDto sampleDto2 = objectMapper.readValue(received.value(), SampleDto.class);

        assertNotNull(received);
        assertEquals(sampleDto, sampleDto2);

    }

}