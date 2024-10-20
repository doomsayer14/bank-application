package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.entity.enums.TransactionStatus;
import com.example.bankingapplication.entity.enums.TransactionType;
import com.example.bankingapplication.exception.ImpermissibleAmountException;
import com.example.bankingapplication.exception.InsufficientBalanceException;
import com.example.bankingapplication.repository.TransactionRepository;
import com.example.bankingapplication.service.AccountService;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;
    private TransactionDto transactionDto;

    private Transaction savedTransaction;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(Money.of(100, "USD")); // Initial balance

        transactionDto = new TransactionDto();
        transactionDto.setAmount(Money.of(50, "USD")); // Amount for transaction
        transactionDto.setToAccount(1L);
        transactionDto.setFromAccount(2L);

        savedTransaction = new Transaction();
        savedTransaction.setAmount(Money.of(50, "USD")); // Amount for transaction
    }

    @Test
    void testDeposit_Success() {
        // Arrange

        savedTransaction.setTransactionType(TransactionType.DEPOSIT);
        savedTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        savedTransaction.setToAccount(1L);
        savedTransaction.setFromAccount(null);

        when(accountService.getAccountById(transactionDto.getToAccount())).thenReturn(account);
        when(transactionTemplate.execute(any())).thenReturn(savedTransaction);


        // Act
        Transaction transaction = transactionService.deposit(transactionDto);

        // Assert
        assertNotNull(transaction);
        assertEquals(savedTransaction.getTransactionType(), transaction.getTransactionType());
        assertEquals(savedTransaction.getTransactionStatus(), transaction.getTransactionStatus());
        assertEquals(savedTransaction.getAmount(), transaction.getAmount());
        assertEquals(savedTransaction.getToAccount(), transaction.getToAccount());
        assertNull(savedTransaction.getFromAccount());

    }

    @Test
    void testDeposit_Failed_NegativeAmount() {
        // Arrange
        transactionDto.setAmount(Money.of(-10, "USD"));

        // Act & Assert
        assertThrows(ImpermissibleAmountException.class, () -> transactionService.deposit(transactionDto));
        verify(transactionRepository).save(any(Transaction.class)); // Verify save was called for failed transaction
    }

    @Test
    void testWithdraw_Success() {
        // Arrange
        savedTransaction.setTransactionType(TransactionType.WITHDRAWAL);
        savedTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        savedTransaction.setToAccount(null);
        savedTransaction.setFromAccount(2L);

        when(accountService.getAccountById(transactionDto.getFromAccount())).thenReturn(account);
        when(transactionTemplate.execute(any())).thenReturn(savedTransaction);

        // Act
        Transaction transaction = transactionService.withdraw(transactionDto);

        // Assert
        assertNotNull(transaction);
        assertEquals(savedTransaction.getTransactionType(), transaction.getTransactionType());
        assertEquals(savedTransaction.getTransactionStatus(), transaction.getTransactionStatus());
        assertEquals(savedTransaction.getAmount(), transaction.getAmount());
        assertEquals(savedTransaction.getFromAccount(), transaction.getFromAccount());
        assertNull(savedTransaction.getToAccount());
    }

    @Test
    void testWithdraw_Failed_InsufficientBalance() {
        // Arrange
        transactionDto.setAmount(Money.of(BigDecimal.valueOf(150.00), "USD")); // Exceeding balance
        when(accountService.getAccountById(transactionDto.getFromAccount())).thenReturn(account);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transactionService.withdraw(transactionDto));
        verify(transactionRepository).save(any(Transaction.class)); // Verify save was called for failed transaction
    }

    @Test
    void testTransfer_Success() {
        // Arrange
        savedTransaction.setTransactionType(TransactionType.TRANSFER);
        savedTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        savedTransaction.setToAccount(1L);
        savedTransaction.setFromAccount(2L);

        Account fromAccount = new Account();
        fromAccount.setId(2L);
        fromAccount.setBalance(Money.of(50, "USD")); // Initial balance for target account

        when(accountService.getAccountById(transactionDto.getFromAccount())).thenReturn(account);
        when(transactionTemplate.execute(any())).thenReturn(savedTransaction);

        // Act
        Transaction transaction = transactionService.transfer(transactionDto);

        // Assert
        assertNotNull(transaction);
        assertEquals(savedTransaction.getTransactionType(), transaction.getTransactionType());
        assertEquals(savedTransaction.getTransactionStatus(), transaction.getTransactionStatus());
        assertEquals(savedTransaction.getAmount(), transaction.getAmount());
        assertEquals(savedTransaction.getToAccount(), transaction.getToAccount());
        assertEquals(savedTransaction.getFromAccount(), transaction.getFromAccount());
    }

    @Test
    void testTransfer_Failed_NegativeAmount() {
        // Arrange
        transactionDto.setAmount(Money.of(-10, "USD"));

        // Act & Assert
        assertThrows(ImpermissibleAmountException.class, () -> transactionService.transfer(transactionDto));
        verify(transactionRepository).save(any(Transaction.class)); // Verify save was called for failed transaction
    }

    @Test
    void testTransfer_Failed_InsufficientBalance() {
        // Arrange
        transactionDto.setAmount(Money.of(150, "USD")); // Exceeding balance
        when(accountService.getAccountById(transactionDto.getFromAccount())).thenReturn(account);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transactionService.transfer(transactionDto));
        verify(transactionRepository).save(any(Transaction.class)); // Verify save was called for failed transaction
    }
}