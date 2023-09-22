package com.example.bankproducer.service;

import com.example.sampledto.SampleDto;
import com.example.stepdto.StepDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountService {


    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public String sendMessage(SampleDto sampleDto) throws JsonProcessingException {

        String orderAsMessage = objectMapper.writeValueAsString(sampleDto);
        kafkaTemplate.send("topic11","kekw", orderAsMessage);

        return "message sent";
    }

    @Transactional
    public String sendMessageStep(StepDto stepDto) throws JsonProcessingException {

        String orderAsMessage = objectMapper.writeValueAsString(stepDto);
        kafkaTemplate.send("topic13","kekw", orderAsMessage);

        return "message sent";
    }
}

