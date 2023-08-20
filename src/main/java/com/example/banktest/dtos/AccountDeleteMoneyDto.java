package com.example.banktest.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDeleteMoneyDto {
    private Long id;
    private int accountNumber;
    private int moneyAmount;
}
