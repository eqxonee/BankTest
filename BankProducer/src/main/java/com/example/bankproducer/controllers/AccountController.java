package com.example.bankproducer.controllers;


import com.example.bankproducer.service.AccountService;


import com.example.sampledto.SampleDto;
import com.example.stepdto.StepDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AccountController {

    private AccountService accountService;

    @PostMapping(value = "kafka-update")
    public void kafkaUpdate(@RequestBody SampleDto sampleDto) throws JsonProcessingException {
        accountService.sendMessage(sampleDto);
    }

    @PostMapping(value = "kafka-update-step")
    public void kafkaUpdateStep(@RequestBody StepDto stepDto) throws JsonProcessingException {
        accountService.sendMessageStep(stepDto);
    }

    @SneakyThrows
    private void sleep(){
        Thread.sleep(Long.parseLong("2000"));
    }
}
