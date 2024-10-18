package com.example.bankingapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The ImpermissibleAmountException can be thrown when a transaction have unappropriated amount
 * to complete a transaction.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImpermissibleAmountException extends RuntimeException {
    public ImpermissibleAmountException(String message) {
        super(message);
    }
}
