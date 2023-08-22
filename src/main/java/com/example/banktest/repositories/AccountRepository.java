package com.example.banktest.repositories;

import com.example.banktest.models.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findAccountById(Long id);

//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query(value = "UPDATE account SET money_amount=(money_amount-:money_amount) WHERE id=:id", nativeQuery = true)
//    void findAccountById2(@Param("money_amount") int money_amount,@Param("id") int id);



    @Query(value = "SELECT money_amount FROM account WHERE id =:id FOR UPDATE",nativeQuery = true)
    int findMoneyAccount(@Param("id") int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE account SET money_amount=:money_amount WHERE id=:id", nativeQuery = true)
    void updateAccount(@Param("money_amount") int money_amount,@Param("id") int id);


    //TODO 2 метода по снятию денег с кошелька , оба @Query , один SELECT , другой UPDATE



}
