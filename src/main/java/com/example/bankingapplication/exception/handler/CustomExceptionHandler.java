package com.example.bankingapplication.exception.handler;

import com.example.bankingapplication.exception.AccountNotFoundException;
import com.example.bankingapplication.exception.ImpermissibleAmountException;
import com.example.bankingapplication.exception.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class if for handling exceptions.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String accountNotFoundException(AccountNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ImpermissibleAmountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String impermissibleAmountException(ImpermissibleAmountException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String insufficientBalanceException(InsufficientBalanceException e) {
        return e.getMessage();
    }
}