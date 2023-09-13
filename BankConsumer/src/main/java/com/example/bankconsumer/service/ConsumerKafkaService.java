package com.example.bankconsumer.service;

import com.example.bankconsumer.dtos.ConsumerDto;
import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumerKafkaService {

    private AccountRepository accountRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int findMoneyAccounts(int id) {

        return accountRepository.findMoneyAccount(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void accountUpdate(SampleDto sampleDto) {

        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(sampleDto.getId())) - sampleDto.getMoneyAmount(), Math.toIntExact(sampleDto.getId()));

        if ( findMoneyAccounts(Math.toIntExact(sampleDto.getId()))< 0) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке");
        }
    }


}
