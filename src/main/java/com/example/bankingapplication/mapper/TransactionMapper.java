package com.example.bankingapplication.mapper;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import org.springframework.stereotype.Component;

/**
 * This class is needed to convert {@link Transaction} to {@link TransactionDto}. Made in Facade style.
 */
@Component
public class TransactionMapper {
    public TransactionDto transactionToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .transactionStatus(transaction.getTransactionStatus())
                .amount(transaction.getAmount())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}