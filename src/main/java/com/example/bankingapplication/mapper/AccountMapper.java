package com.example.bankingapplication.mapper;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import org.springframework.stereotype.Component;

/**
 * This class is needed to convert {@link Account} to {@link AccountDto}. Made in Facade style.
 */
@Component
public class AccountMapper {
    public AccountDto accountToAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .balance(account.getBalance())
                .number(account.getNumber())
                .build();
    }
}
