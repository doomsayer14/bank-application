package com.example.bankingapplication.mapper;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountMapperTest {

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper();
    }

    @Test
    void testAccountToAccountDto() {
        // Arrange: Create an Account object with sample data
        Account account = new Account();
        account.setId(1L);
        account.setName("John Doe");
        account.setBalance(Money.of(100, "USD"));
        account.setNumber("+1 123 456 7890");

        // Act: Call the method under test
        AccountDto accountDto = accountMapper.accountToAccountDto(account);

        // Assert: Check if the mapping is correct
        assertNotNull(accountDto);
        assertEquals(1L, accountDto.getId());
        assertEquals("John Doe", accountDto.getName());
        assertEquals(Money.of(100, "USD"), accountDto.getBalance());
        assertEquals("+1 123 456 7890", accountDto.getNumber());
    }
}