package com.example.bankconsumer.repositories;

import com.example.bankconsumer.models.Account;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
class AccountRepositoryTest {

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

    @Test
    void findMoneyAccount() {
        Account account = new Account(1L,345,20000);
        accountRepository.save(account);

        int money = accountRepository.findMoneyAccount(1);

        assertEquals(money,account.getMoneyAmount());
    }

    @Test
    @Transactional
    void updateAccount() {

        Account account = new Account(1L,345,20000);
        accountRepository.save(account);

        int money = accountRepository.findMoneyAccount(1);
        int money2 = 5000;

        accountRepository.updateAccount(money - money2,1);
        Optional<Account> newMoney = accountRepository.findAccountById(1L);

        assertEquals(newMoney.get().getMoneyAmount(),15000);

        //TODO Если в кафке есть что-то , считываю с кафки и обновляю бд!
    }
}