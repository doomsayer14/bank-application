package com.example.bankingapplication.dto;

import com.example.bankingapplication.entity.enums.TransactionStatus;
import com.example.bankingapplication.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {
    private Long id;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private Money amount;
    private Long fromAccount;
    private Long toAccount;
    private LocalDateTime transactionDate;
}
