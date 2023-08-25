package com.example.banktest.service;

import com.example.banktest.dtos.AccountDeleteMoneyDto;
import com.example.banktest.dtos.AccountGetAllDto;
import com.example.banktest.dtos.AccountUpdateMoneyDto;
import com.example.banktest.dtos.ProducerDto;
import com.example.banktest.models.Account;
import com.example.banktest.repositories.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private ModelMapper modelMapper;
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


//    @PersistenceContext
//    EntityManager entityManager;
    //private TransactionTemplate transactionTemplate;


    public List<AccountGetAllDto> getAll() {

        List<Account> orders = accountRepository.findAll();

        return orders.stream().map(order -> modelMapper
                        .map(order, AccountGetAllDto.class))
                .collect(Collectors.toList());
    }

    public void updateMoneyById(AccountUpdateMoneyDto accountUpdateMoneyDto) {
        Optional<Account> findAccount = accountRepository.findById(accountUpdateMoneyDto.getId());

        findAccount.map(m -> {
            m.setMoneyAmount(m.getMoneyAmount() + accountUpdateMoneyDto.getMoneyAmount());
            return m;
        });

        accountRepository.save(findAccount.get());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
    public void deleteMoneyById(AccountDeleteMoneyDto accountDeleteMoneyDto) {

        Optional<Account> findAccount = accountRepository.findAccountById(accountDeleteMoneyDto.getId());

        findAccount.map(m -> {
            if (m.getMoneyAmount() < accountDeleteMoneyDto.getMoneyAmount()) {
                throw new RuntimeException("Баланс не может быть ниже нуля");
            }
            m.setMoneyAmount(m.getMoneyAmount() - accountDeleteMoneyDto.getMoneyAmount());
            return m;
        });

        //accountRepository.save(findAccount.get());
    }


//    public int findMoneyAccounts(int id) {
//
//        return accountRepository.findMoneyAccount(id);
//    }
//
//    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
//    public void updateMoney(AccountUpdateMoneyDto accountUpdateMoneyDto) {
////        String kafka = gson.toJson(accountUpdateMoneyDto);
////
////        kafkaTemplate.send("topic3", kafka);
////        boolean result = kafkaTemplate.executeInTransaction(t ->{
////            t.send("topic3", kafka);
////            return true;
////        });
//
//            accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(accountUpdateMoneyDto.getId())) - accountUpdateMoneyDto.getMoneyAmount(), Math.toIntExact(accountUpdateMoneyDto.getId()));
//
//    }


    private String writeValueAsString(ProducerDto producerDto) {
        try {
            return objectMapper.writeValueAsString(producerDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + producerDto.toString());
        }
    }
    @Transactional
    public String sendMessage(ProducerDto producerDto) throws JsonProcessingException {
        //String kafka = gson.toJson(producerDto);
        String orderAsMessage = objectMapper.writeValueAsString(producerDto);
        kafkaTemplate.send("topic3", orderAsMessage);
        return "message sent";
    }
}

