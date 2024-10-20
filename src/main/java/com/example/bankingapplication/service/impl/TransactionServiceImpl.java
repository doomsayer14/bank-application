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
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionTemplate transactionTemplate;


    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService, TransactionTemplate transactionTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.transactionTemplate = transactionTemplate;
    }

    public Transaction deposit(TransactionDto transactionDto) {
        if (transactionDto.getAmount().isNegativeOrZero()) {
            Transaction transaction = buildTransaction(TransactionType.DEPOSIT, TransactionStatus.FAILED,
                    transactionDto.getAmount(), null,
                    transactionDto.getToAccount());

            transactionRepository.save(transaction);
            log.error("Deposit failed {}", transaction);
            throw new ImpermissibleAmountException("Transaction with id = " + transactionDto.getId() +
                    "has zero or negative amount of money.");
        }

        Account account = accountService.getAccountById(transactionDto.getToAccount());
        return transactionTemplate.execute(status -> {
            account.getBalance().add(transactionDto.getAmount());
            accountService.updateAccount(account);
            Transaction transaction = buildTransaction(TransactionType.DEPOSIT, TransactionStatus.SUCCESSFUL,
                    transactionDto.getAmount(), null,
                    transactionDto.getToAccount());
            transactionRepository.save(transaction);

            log.info("Deposit went successfully {}", transaction);
            return transactionRepository.save(transaction);
        });
    }

    public Transaction withdraw(TransactionDto transactionDto) {
        Account account = accountService.getAccountById(transactionDto.getFromAccount());
        if (transactionDto.getAmount().isGreaterThan(account.getBalance())) {
            Transaction transaction = buildTransaction(TransactionType.WITHDRAWAL, TransactionStatus.FAILED,
                    transactionDto.getAmount(), transactionDto.getFromAccount(),
                    null);
            transactionRepository.save(transaction);

            log.error("Withdrawal failed {}", transaction);
            throw new InsufficientBalanceException("The account with id = {}" + account.getId() +
                    "doesn't have enough balance to make a transaction");
        }

        return transactionTemplate.execute(status -> {
            account.getBalance().subtract(transactionDto.getAmount());
            accountService.updateAccount(account);
            Transaction transaction = this.buildTransaction(TransactionType.WITHDRAWAL, TransactionStatus.SUCCESSFUL,
                    transactionDto.getAmount(), transactionDto.getFromAccount(),
                    null);
            log.info("Withdrawal went successfully {}", transaction);
            return transactionRepository.save(transaction);
        });
    }

    public Transaction transfer(TransactionDto transactionDto) {
        //checks
        if (transactionDto.getAmount().isNegativeOrZero()) {
            Transaction transaction = buildTransaction(TransactionType.TRANSFER, TransactionStatus.FAILED,
                    transactionDto.getAmount(), transactionDto.getFromAccount(),
                    transactionDto.getToAccount());
            transactionRepository.save(transaction);

            log.error("Transfer failed {}", transaction);
            throw new ImpermissibleAmountException("Transaction with id = " + transactionDto.getId()
                    + "has zero or negative amount of money.");
        }
        Account accountFrom = accountService.getAccountById(transactionDto.getFromAccount());
        if (transactionDto.getAmount().isGreaterThan(accountFrom.getBalance())) {
            Transaction transaction = buildTransaction(TransactionType.TRANSFER, TransactionStatus.FAILED,
                    transactionDto.getAmount(), transactionDto.getFromAccount(),
                    transactionDto.getToAccount());

            transactionRepository.save(transaction);
            log.error("Transfer failed {}", transaction);
            throw new InsufficientBalanceException("The account with id = {}" + accountFrom.getId() +
                    "doesn't have enough balance to make a transaction");
        }

        //open transaction
        return transactionTemplate.execute(status -> {
            //withdrawing
            accountFrom.getBalance().subtract(transactionDto.getAmount());
            accountService.updateAccount(accountFrom);
            //deposit
            Account accountTo = accountService.getAccountById(transactionDto.getToAccount());
            accountTo.getBalance().add(transactionDto.getAmount());
            accountService.updateAccount(accountTo);

            Transaction transaction = this.buildTransaction(TransactionType.TRANSFER, TransactionStatus.SUCCESSFUL,
                    transactionDto.getAmount(), transactionDto.getFromAccount(),
                    transactionDto.getToAccount());
            log.info("Transfer went successfully {}", transaction);
            return transactionRepository.save(transaction);
        });
    }

    private Transaction buildTransaction(TransactionType transactionType, TransactionStatus transactionStatus,
                                        Money amount, Long fromAccount, Long toAccount) {
        return Transaction.builder()
                .transactionType(transactionType)
                .transactionStatus(transactionStatus)
                .amount(amount)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .transactionDate(LocalDateTime.now())
                .build();
    }
}