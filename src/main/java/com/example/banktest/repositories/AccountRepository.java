package com.example.banktest.repositories;

import com.example.banktest.dtos.AccountDeleteMoneyDto;
import com.example.banktest.models.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findAccountById(Long id);

}
