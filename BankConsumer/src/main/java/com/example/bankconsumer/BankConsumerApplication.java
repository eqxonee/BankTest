package com.example.bankconsumer;

import com.example.bankconsumer.repositories.AccountRepository;
import com.example.bankconsumer.service.ConsumerService;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BankConsumerApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(BankConsumerApplication.class, args);
    }

}
