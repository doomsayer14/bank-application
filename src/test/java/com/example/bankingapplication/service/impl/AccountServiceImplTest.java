package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.exception.AccountNotFoundException;
import com.example.bankingapplication.repository.AccountRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final BigDecimal initialBalance = BigDecimal.valueOf(10); // default value for testing

    private final String initialCurrency = "USD"; // default value for testing

    @BeforeEach
    void setUp() {
        // Initialize the service with mocked values
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void testGetAccountById_AccountFound() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        Account account = accountService.getAccountById(accountId);

        // Assert
        assertNotNull(account);
        assertEquals(accountId, account.getId());
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    void testUpdateAccount() {
        // Arrange
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setId(accountId);
        accountDto.setName("Updated Name");
        accountDto.setNumber("1234567890");
        accountDto.setBalance(Money.of(999, "EUR"));

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setName("Old Name");
        existingAccount.setNumber("+1 123 456 7890");
        existingAccount.setBalance(Money.of(initialBalance, initialCurrency));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        // Act
        Account updatedAccount = accountService.updateAccount(accountDto);

        // Assert
        assertEquals("Updated Name", updatedAccount.getName());
        assertEquals("1234567890", updatedAccount.getNumber());
        assertEquals(Money.of(999, "EUR"), updatedAccount.getBalance());
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void testCreateAccount() {
        // Arrange
        AccountDto accountDto = new AccountDto();
        accountDto.setName("New Account");
        accountDto.setNumber("1234567890");

        Account createdAccount = new Account();
        createdAccount.setId(1L);
        createdAccount.setName("New Account");
        createdAccount.setNumber("1234567890");
        createdAccount.setBalance(Money.of(initialBalance, initialCurrency));

        when(accountRepository.save(any(Account.class))).thenReturn(createdAccount);

        ReflectionTestUtils.setField(accountService, "initialCurrency", "USD");
        ReflectionTestUtils.setField(accountService, "initialBalance", BigDecimal.valueOf(10));

        // Act
        Account account = accountService.createAccount(accountDto);

        // Assert
        assertNotNull(account);
        assertEquals("New Account", account.getName());
        assertEquals("1234567890", account.getNumber());
        assertEquals(Money.of(initialBalance, initialCurrency), account.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testGetAccountByNumber_AccountFound() {
        // Arrange
        String accountNumber = "1234567890";
        Account mockAccount = new Account();
        mockAccount.setNumber(accountNumber);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        // Act
        Account account = accountService.getAccountByNumber(accountNumber);

        // Assert
        assertNotNull(account);
        assertEquals(accountNumber, account.getNumber());
    }

    @Test
    void testGetAccountByNumber_AccountNotFound() {
        // Arrange
        String accountNumber = "123456";
        when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByNumber(accountNumber));
    }

    @Test
    void testGetAllAccounts() {
        // Arrange
        List<Account> mockAccounts = List.of(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(mockAccounts);

        // Act
        List<Account> accounts = accountService.getAllAccounts();

        // Assert
        assertEquals(2, accounts.size());
        verify(accountRepository).findAll();
    }

    @Test
    void testDeleteAccount() {
        // Arrange
        Long accountId = 1L;

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository).deleteById(accountId);
    }
}