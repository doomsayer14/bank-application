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
import com.example.bankingapplication.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public Transaction deposit(TransactionDto transactionDto) {
        if (transactionDto.getAmount().isNegativeOrZero()) {
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.DEPOSIT)
                    .transactionStatus(TransactionStatus.FAILED)
                    .amount(transactionDto.getAmount())
                    .toAccount(transactionDto.getToAccount())
                    .transactionDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(transaction);
            log.error("Deposit failed {}", transaction);
            throw new ImpermissibleAmountException("Transaction with id = " + transactionDto.getId() +
                    "has zero or negative amount of money.");
        }

        Account account = accountService.getAccountById(transactionDto.getToAccount());
        account.getBalance().add(transactionDto.getAmount());
        accountService.updateAccount(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESSFUL)
                .amount(transactionDto.getAmount())
                .toAccount(transactionDto.getToAccount())
                .transactionDate(LocalDateTime.now())
                .build();

        log.info("Deposit went successfully {}", transaction);
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(TransactionDto transactionDto) {
        Account account = accountService.getAccountById(transactionDto.getFromAccount());
        if (transactionDto.getAmount().isGreaterThan(account.getBalance())) {
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.WITHDRAWAL)
                    .transactionStatus(TransactionStatus.FAILED)
                    .amount(transactionDto.getAmount())
                    .fromAccount(transactionDto.getFromAccount())
                    .transactionDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(transaction);
            log.error("Withdrawal failed {}", transaction);
            throw new InsufficientBalanceException("The account with id = {}" + account.getId() +
                    "doesn't have enough balance to make a transaction");
        }
        account.getBalance().subtract(transactionDto.getAmount());
        accountService.updateAccount(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .transactionStatus(TransactionStatus.SUCCESSFUL)
                .amount(transactionDto.getAmount())
                .fromAccount(transactionDto.getFromAccount())
                .transactionDate(LocalDateTime.now())
                .build();

        log.info("Withdrawal went successfully {}", transaction);
        return transactionRepository.save(transaction);
    }

    public Transaction transfer(TransactionDto transactionDto) {
        //checks
        if (transactionDto.getAmount().isNegativeOrZero()) {
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.DEPOSIT)
                    .transactionStatus(TransactionStatus.FAILED)
                    .amount(transactionDto.getAmount())
                    .fromAccount(transactionDto.getFromAccount())
                    .toAccount(transactionDto.getToAccount())
                    .transactionDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(transaction);
            log.error("Transfer failed {}", transaction);
            throw new ImpermissibleAmountException("Transaction with id = " + transactionDto.getId()
                    + "has zero or negative amount of money.");
        }
        Account accountFrom = accountService.getAccountById(transactionDto.getFromAccount());
        if (transactionDto.getAmount().isGreaterThan(accountFrom.getBalance())) {
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.WITHDRAWAL)
                    .transactionStatus(TransactionStatus.FAILED)
                    .amount(transactionDto.getAmount())
                    .fromAccount(transactionDto.getFromAccount())
                    .toAccount(transactionDto.getToAccount())
                    .transactionDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(transaction);
            log.error("Transfer failed {}", transaction);
            throw new InsufficientBalanceException("The account with id = {}" + accountFrom.getId() +
                    "doesn't have enough balance to make a transaction");
        }
        //withdrawing
        accountFrom.getBalance().subtract(transactionDto.getAmount());
        accountService.updateAccount(accountFrom);
        //deposit
        Account accountTo = accountService.getAccountById(transactionDto.getToAccount());
        accountTo.getBalance().add(transactionDto.getAmount());
        accountService.updateAccount(accountTo);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionStatus(TransactionStatus.SUCCESSFUL)
                .amount(transactionDto.getAmount())
                .fromAccount(transactionDto.getFromAccount())
                .toAccount(transactionDto.getToAccount())
                .transactionDate(LocalDateTime.now())
                .build();
        log.info("Transfer went successfully {}", transaction);
        return transactionRepository.save(transaction);
    }

    private
}
