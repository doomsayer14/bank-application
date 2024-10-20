package com.example.bankingapplication.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.entity.enums.TransactionStatus;
import com.example.bankingapplication.entity.enums.TransactionType;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TransactionMapperTest {

    private TransactionMapper transactionMapper; // Class under test

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper(); // Initialize TransactionMapper
    }

    @Test
    void testTransactionToTransactionDto() {
        // Arrange: Create a Transaction object with sample data
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setAmount(Money.of(500, "USD"));
        transaction.setFromAccount(1L);
        transaction.setToAccount(2L);
        transaction.setTransactionDate(LocalDateTime.parse("2024-10-20T18:04:40.788658"));

        // Act: Call the method under test
        TransactionDto transactionDto = transactionMapper.transactionToTransactionDto(transaction);

        // Assert: Check if the mapping is correct
        assertNotNull(transactionDto);
        assertEquals(1L, transactionDto.getId());
        assertEquals(TransactionType.DEPOSIT, transactionDto.getTransactionType());
        assertEquals(TransactionStatus.SUCCESSFUL, transactionDto.getTransactionStatus());
        assertEquals(Money.of(500, "USD"), transactionDto.getAmount());
        assertEquals(1L, transactionDto.getFromAccount());
        assertEquals(2L, transactionDto.getToAccount());
        assertEquals(LocalDateTime.parse("2024-10-20T18:04:40.788658"), transactionDto.getTransactionDate());
    }
}
