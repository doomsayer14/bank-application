package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {
    /**
     * Deposit funds to an account.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    @Transactional
    Transaction deposit(TransactionDto transactionDto);

    /**
     * Withdraw funds from an account.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    @Transactional
    Transaction withdraw(TransactionDto transactionDto);

    /**
     * Transfer funds from account to another.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    @Transactional
    Transaction transfer(TransactionDto transactionDto);
}
