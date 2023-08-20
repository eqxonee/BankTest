package com.example.banktest.service;

import com.example.banktest.dtos.AccountDeleteMoneyDto;
import com.example.banktest.dtos.AccountGetAllDto;
import com.example.banktest.dtos.AccountUpdateMoneyDto;
import com.example.banktest.models.Account;
import com.example.banktest.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.retry.annotation.Retryable;
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
}
