package com.example.banktest.controllers;


import com.example.banktest.dtos.AccountDeleteMoneyDto;
import com.example.banktest.dtos.AccountGetAllDto;
import com.example.banktest.dtos.AccountUpdateMoneyDto;
import com.example.banktest.models.Account;
import com.example.banktest.service.AccountService;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {

    private AccountService accountService;


    @GetMapping("get-all")
    public List<AccountGetAllDto> getAll(){
        return accountService.getAll();
    }

    @PostMapping(value = "add-money")
    public void addMoney(@RequestBody AccountUpdateMoneyDto accountUpdateMoneyDto) {
        accountService.updateMoneyById(accountUpdateMoneyDto);
        sleep();

    }

    @PostMapping(value = "delete-money")
    public void deleteMoney(@RequestBody AccountDeleteMoneyDto accountDeleteMoneyDto) {
        //accountService.deleteMoney2(accountDeleteMoneyDto);
    }

    @GetMapping("get-money-by-id/{id}")
    public void getMoneyById(@PathVariable int id){
        accountService.findMoneyAccounts(id);
    }

    @PostMapping(value = "update-money")
    public void updateMoney(@RequestBody AccountUpdateMoneyDto accountUpdateMoneyDto) {
        accountService.updateMoney(accountUpdateMoneyDto);
    }

    @SneakyThrows
    private void sleep(){
        Thread.sleep(Long.parseLong("2000"));
    }
}
