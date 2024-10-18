package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.AccountDto;
import com.example.bankingapplication.entity.Account;
import com.example.bankingapplication.exception.AccountNotFoundException;

import java.util.List;

public interface AccountService {

    /**
     * Gets an account by id.
     *
     * @param id An id of needed account.
     * @return Account.
     * @throws {@link AccountNotFoundException} in case when there is no existing account with
     *                provided id.
     */
    Account getAccountById(Long id);

    /**
     * Updates an account.
     *
     * @param accountDto Account to be updated.
     * @return Updated account.
     */
    Account updateAccount(AccountDto accountDto);

    /**
     * Overload for {@link com.example.bankingapplication.service.TransactionService}
     *
     * @param account Account to be updated.
     * @return Updated account.
     */
    Account updateAccount(Account account);

    /**
     * Saves new account in database.
     *
     * @param accountDto An account to be created.
     * @return Saved account.
     */
    Account createAccount(AccountDto accountDto);

    /**
     * Get account by its number.
     *
     * @param number The number by which an account is searched.
     * @return An account with specified number
     * @throws {@link AccountNotFoundException} in case when there is no existing account with
     *                provided number.
     */
    Account getAccountByNumber(String number);

    /**
     * Finds all existing accounts in database.
     *
     * @return list of accounts.
     */
    List<Account> getAllAccounts();

    /**
     * Deletes and account by id.
     *
     * @param id An id of an account to be deleted.
     */
    void deleteAccount(Long id);
}
