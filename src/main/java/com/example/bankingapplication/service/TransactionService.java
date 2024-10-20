package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;

public interface TransactionService {
    /**
     * Deposit funds to an account.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    Transaction deposit(TransactionDto transactionDto);

    /**
     * Withdraw funds from an account.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    Transaction withdraw(TransactionDto transactionDto);

    /**
     * Transfer funds from account to another.
     *
     * @param transactionDto transaction to be completed.
     * @return Completed transaction.
     */
    Transaction transfer(TransactionDto transactionDto);

}
