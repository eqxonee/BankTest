package com.example.bankconsumer.repositories;

import com.example.bankconsumer.models.Account;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
//@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=9092"})
//@SpringBootTest
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableJpaRepositories(basePackages = {"com.example.bankconsumer.repositories"})
//@EntityScan(basePackages = {"com.example.bankconsumer.models"})
class AccountRepositoryTest {

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