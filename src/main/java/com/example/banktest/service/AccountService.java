package com.example.banktest.service;

import com.example.banktest.dtos.AccountDeleteMoneyDto;
import com.example.banktest.dtos.AccountGetAllDto;
import com.example.banktest.dtos.AccountUpdateMoneyDto;
import com.example.banktest.models.Account;
import com.example.banktest.repositories.AccountRepository;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private Gson gson;

//    @PersistenceContext
//    EntityManager entityManager;
    //private TransactionTemplate transactionTemplate;


    public List<AccountGetAllDto> getAll(){

        List<Account> orders = accountRepository.findAll();

        return orders.stream().map(order -> modelMapper
                        .map(order, AccountGetAllDto.class))
                .collect(Collectors.toList());
    }

    public void updateMoneyById(AccountUpdateMoneyDto accountUpdateMoneyDto){
        Optional<Account> findAccount = accountRepository.findById(accountUpdateMoneyDto.getId());

        findAccount.map(m -> {
            m.setMoneyAmount(m.getMoneyAmount() + accountUpdateMoneyDto.getMoneyAmount());
            return m;
        });

        accountRepository.save(findAccount.get());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
    public void deleteMoneyById(AccountDeleteMoneyDto accountDeleteMoneyDto){

        Optional<Account> findAccount = accountRepository.findAccountById(accountDeleteMoneyDto.getId());

        findAccount.map(m -> {
            if(m.getMoneyAmount() < accountDeleteMoneyDto.getMoneyAmount()){
                throw new RuntimeException("Баланс не может быть ниже нуля");
            }
            m.setMoneyAmount(m.getMoneyAmount() - accountDeleteMoneyDto.getMoneyAmount());
            return m;
        });

        //accountRepository.save(findAccount.get());
    }


    public int findMoneyAccounts(int id){

        return accountRepository.findMoneyAccount(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
    public void updateMoney(AccountUpdateMoneyDto accountUpdateMoneyDto){

        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(accountUpdateMoneyDto.getId())) - accountUpdateMoneyDto.getMoneyAmount(), Math.toIntExact(accountUpdateMoneyDto.getId()));

        String kafka = gson.toJson(accountUpdateMoneyDto);
        kafkaTemplate.send("topic2", kafka);

        //return update;

    }
//    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3)
//    public String deleteMoney2(AccountDeleteMoneyDto accountDeleteMoneyDto){
//
//        accountRepository.findAccountById2(accountDeleteMoneyDto.getMoneyAmount(), Math.toIntExact(accountDeleteMoneyDto.getId()));
//
//
//        return "Record updated successfully using @Modifiying and @query Named Parameter";





//        Query query = (Query) entityManager.createNativeQuery("UPDATE account SET money_amount =:money_amount WHERE id =:id");
//        query.setParameter("money_amount",findAccount2.get().getMoneyAmount());
//        query.setParameter("id",accountDeleteMoneyDto.getId());
//        query.executeUpdate();


    }

