package com.example.bankconsumer.service;

import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@DataJpaTest
@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"})
class ConsumerServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @MockBean
    private EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    public void setUp() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafka));
        // Настройте необходимые свойства для producerProps
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }
//    MockProducer<String, String> mockProducer =
//            new MockProducer<>(true, new StringSerializer(), new StringSerializer());

    @BeforeEach
    void producer() throws JsonProcessingException {
        SampleDto sampleDto = new SampleDto(1L,1000);

        String orderAsMessage = objectMapper.writeValueAsString(sampleDto);
        kafkaTemplate.send("topic10",orderAsMessage);


    }
//    @Test
//    int findMoneyAccounts() {
//        Account account = new Account(1L,345,20000);
//        accountRepository.save(account);
//
//        int money = accountRepository.findMoneyAccount(1);
//
//        assertEquals(money,account.getMoneyAmount());
//        return money;
//    }


    BlockingQueue<ConsumerRecord<String, String>> consumerRecords = new LinkedBlockingQueue<>();
    @KafkaListener(topics = "topic10", groupId = "group111", concurrency = "3")
    public void listen(ConsumerRecord consumerRecord) throws JsonProcessingException {
        consumerRecords.add(consumerRecord);
    }
    @Test
    void listen() throws JsonProcessingException, InterruptedException {

        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);
        SampleDto sampleDto = objectMapper.readValue(received.value(),SampleDto.class);

        accountRepository.updateAccount(20000 - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        Optional<Account> newMoney = accountRepository.findAccountById(1L);

        assertEquals(newMoney.get().getMoneyAmount(),19000);

        //Бд ложится!

    }
}