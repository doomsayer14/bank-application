package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.mapper.TransactionMapper;
import com.example.bankingapplication.service.impl.TransactionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for {@link Transaction}.
 */

@RestController
@RequestMapping("bank/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionServiceImpl;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionServiceImpl transactionServiceImpl, TransactionMapper transactionMapper) {
        this.transactionServiceImpl = transactionServiceImpl;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Endpoint which can be called to make a deposit. Field fromAccount is necessary.
     *
     * @param transactionDto The transaction to be made.
     * @return ResponseEntity<TransactionDto> with successful transaction.
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionServiceImpl.deposit(transactionDto);
        TransactionDto commitedTransactionDto = transactionMapper.transactionToTransactionDto(transaction);
        return ResponseEntity.ok(commitedTransactionDto);
    }

    /**
     * Endpoint which can be called to make a withdrawal. Field toAccount is necessary.
     *
     * @param transactionDto The transaction to be made.
     * @return ResponseEntity<TransactionDto> with successful transaction.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionServiceImpl.withdraw(transactionDto);
        TransactionDto commitedTransactionDto = transactionMapper.transactionToTransactionDto(transaction);
        return ResponseEntity.ok(commitedTransactionDto);
    }

    /**
     * Endpoint which can be called to make a transfer. Both fields toAccount and fromAccount
     * is necessary.
     *
     * @param transactionDto The transaction to be made.
     * @return ResponseEntity<TransactionDto> with successful transaction.
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionServiceImpl.transfer(transactionDto);
        TransactionDto commitedTransactionDto = transactionMapper.transactionToTransactionDto(transaction);
        return ResponseEntity.ok(commitedTransactionDto);
    }
}
