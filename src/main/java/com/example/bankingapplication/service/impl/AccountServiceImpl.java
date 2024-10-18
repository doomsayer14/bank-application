package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.exception.AccountNotFoundException;
import com.example.bankingapplication.repository.AccountRepository;
import com.example.bankingapplication.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for {@link Account}.
 */
@Slf4j
@Service
public final class AccountServiceImpl implements AccountService {

    @Value("${account.initial.balance}")
    private BigDecimal initialBalance;

    @Value("${account.initial.currency}")
    private String initialCurrency;

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException
                        ("The account cannot be found for id" + id));
    }

    @Override
    public Account updateAccount(AccountDto accountDto) {
        Account account = getAccountById(accountDto.getId());
        if (!StringUtils.isBlank(accountDto.getNumber())) {
            account.setName(accountDto.getName());
        }
        if (!StringUtils.isBlank(accountDto.getNumber())) {
            account.setNumber(accountDto.getNumber());
        }
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }
        log.info("Account updated {}", account);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        log.info("Account updated {}", account);
        return accountRepository.save(account);
    }

    @Override
    public Account createAccount(AccountDto accountDto) {
        Account account = Account.builder()
                .name(accountDto.getName())
                .number(accountDto.getNumber())
                .balance(Money.of(initialBalance, initialCurrency))
                .build();
        log.info("Creating account {}", account);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByAccountNumber(number)
                .orElseThrow(() -> new AccountNotFoundException
                        ("The account cannot be found for number" + number));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
