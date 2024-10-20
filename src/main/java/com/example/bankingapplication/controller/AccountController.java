package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.mapper.AccountMapper;
import com.example.bankingapplication.service.impl.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for {@link Account}.
 */

@RestController
@RequestMapping("bank/account")
public class AccountController {

    private final AccountServiceImpl accountServiceImpl;
    private final AccountMapper accountMapper;

    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
        this.accountMapper = new AccountMapper();
    }

    /**
     * Endpoint which can be called to get an Account by id.
     *
     * @param accountId The id of needed account.
     * @return An account with specified id.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("accountId") String accountId) {
        Account account = accountServiceImpl.getAccountById(Long.parseLong(accountId));
        AccountDto accountDto = accountMapper.accountToAccountDto(account);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Endpoint which can be called to get an Account by number.
     *
     * @param number The number of the seeked account.
     * @return ResponseEntity<AccountDto> with new id and 200 HTTP code.
     */
    @GetMapping("/")
    public ResponseEntity<AccountDto> getAccountByNumber(@RequestParam() String number) {
        Account account = accountServiceImpl.getAccountByNumber(number);
        AccountDto accountDto = accountMapper.accountToAccountDto(account);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Endpoint which can be called to get all Accounts.
     *
     * @return ResponseEntity<List<AccountDto>> with new id and 200 HTTP code.
     */
    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtoList = accountServiceImpl.getAllAccounts()
                .stream()
                .map(accountMapper::accountToAccountDto)
                .toList();
        return ResponseEntity.ok(accountDtoList);
    }

    /**
     * Endpoint which can be called to create new Account.
     *
     * @param account The account to be created.
     * @return ResponseEntity<Object> with new id and 201 HTTP code.
     */
    @PostMapping("/")
    public ResponseEntity<Object> createAccount(@RequestBody @Validated AccountDto account,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList(), HttpStatus.BAD_REQUEST);
        }
        Account createdAccount = accountServiceImpl.createAccount(account);
        AccountDto accountDto = accountMapper.accountToAccountDto(createdAccount);
        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint which can be called to update an Account.
     *
     * @return ResponseEntity<Object> with updated fields and 200 HTTP code.
     */
    @PostMapping("/update")
    public ResponseEntity<Object> updateAccount(@RequestBody @Validated AccountDto accountDto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList(), HttpStatus.BAD_REQUEST);
        }
        Account updatedAccount = accountServiceImpl.updateAccount(accountDto);
        AccountDto updatedAccountDto = accountMapper.accountToAccountDto(updatedAccount);
        return ResponseEntity.ok(updatedAccountDto);
    }

    /**
     * Endpoint which can be called to create new Account.
     *
     * @param accountId The account to be deleted.
     * @return ResponseEntity<AccountDto> with new id and 201 HTTP code.
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("accountId") String accountId) {
        accountServiceImpl.deleteAccount(Long.parseLong(accountId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
