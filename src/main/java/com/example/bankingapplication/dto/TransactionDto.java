package com.example.bankingapplication.dto;

import com.example.bankingapplication.entity.enums.TransactionStatus;
import com.example.bankingapplication.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private Money amount;
    private Long fromAccount;
    private Long toAccount;
    private LocalDateTime transactionDate;
}
