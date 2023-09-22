package com.example.bankconsumer.service;

import com.example.bankconsumer.dtos.ConsumerDto;
import com.example.bankconsumer.models.Account;
import com.example.bankconsumer.repositories.AccountRepository;
import com.example.sampledto.SampleDto;
import com.example.stepdto.StepDto;
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
    public void accountUpdate(StepDto stepDto) {

        if ( findMoneyAccounts(Math.toIntExact(stepDto.getId()))< 0) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке");
        }

        accountRepository.updateAccount(findMoneyAccounts(Math.toIntExact(stepDto.getId())) - stepDto.getMoneyAmount(), Math.toIntExact(stepDto.getId()));
        accountRepository.updateAccountStep(stepDto.getStep(), Math.toIntExact(stepDto.getId()));

    }


}
