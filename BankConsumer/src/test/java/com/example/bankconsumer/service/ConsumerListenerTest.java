package com.example.bankconsumer.service;

import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@DataJpaTest
@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"})
class ConsumerListenerTest {

    @Autowired
    AccountRepository accountRepository;

    SampleDto sampleDto = new SampleDto(1L,1000);
    @Test
    void listen() throws IOException, InterruptedException {
        Account account = new Account(1L,345,20000);
        accountRepository.save(account);

        accountRepository.updateAccount(20000 - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        Optional<Account> newMoney = accountRepository.findAccountById(1L);

        assertEquals(newMoney.get().getMoneyAmount(),19000);

        //Бд ложится если поставить @SpringBootTest!

    }
}