package com.example.bankproducer.service;

import com.example.bankproducer.dtos.ProducerDto;
import com.example.bankproducer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 3,brokerProperties = {"listeners=PLAINTEXT://localhost:29092","port=9092"})
//@AutoConfigureMockMvc
//@TestConfiguration
class AccountServiceTest {


//    @MockBean
    @Autowired
    private AccountService accountService;

//    @Autowired
//    private KafkaConsumer consumer;

//    @MockBean
//    private KafkaTemplate<String,String> kafkaTemplate;

//    @MockBean
//    private ObjectMapper objectMapper;

    @Test
    void sendMessage() throws JsonProcessingException {

        //AccountService accountService = new AccountService(null,null,null,null);

        String data = "Sending with our own simple KafkaProducer";

        SampleDto sampleDto = new SampleDto(3L,1000);

        accountService.sendMessage(sampleDto);



//        String orderAsMessage = objectMapper.writeValueAsString(sampleDto);
//        kafkaTemplate.send(topic,"kekw",orderAsMessage);

    }
}