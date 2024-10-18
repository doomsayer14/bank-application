package com.example.bankingapplication.dto;

import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@Builder
public class AccountDto {
    private Long id;
    private String name;
    private String number;
    private Money balance;
}
