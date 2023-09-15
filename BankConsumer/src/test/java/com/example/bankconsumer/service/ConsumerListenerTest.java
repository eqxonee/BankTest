package com.example.bankconsumer.service;

import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@DataJpaTest
//@AutoConfigureMockMvc
//@DirtiesContext
//@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"})
@Testcontainers
@SpringBootTest
class ConsumerListenerTest {

    public static Network network = Network.newNetwork();
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine3.18")
            .withDatabaseName("java_bank_db")
            .withUsername("java_bank_user")
            .withPassword("12345")
            .withNetwork(network);

    @Container
    public static KafkaContainer kafka1 = new KafkaContainer("6.2.4")
            .withNetwork(network);

    @Container
    public static KafkaContainer kafka2 = new KafkaContainer("6.2.4")
            .withNetwork(network);

    @DynamicPropertySource
    public static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafka1.getBootstrapServers() + "," + kafka2.getBootstrapServers());
    }

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.sql.init.schema-locations", () -> "classpath:schema.sql");
    }

    @Autowired
    AccountRepository accountRepository;

    SampleDto sampleDto = new SampleDto(1L,1000);
    @Test
    @Transactional
    void listen() throws IOException, InterruptedException {
        Account account = new Account(1L,345,20000);
        accountRepository.save(account);

        accountRepository.updateAccount(20000 - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        Optional<Account> newMoney = accountRepository.findAccountById(1L);

        assertEquals(newMoney.get().getMoneyAmount(),19000);

        //Бд ложится если поставить @SpringBootTest!

    }
}