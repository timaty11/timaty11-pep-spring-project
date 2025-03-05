package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Retrieve all accounts from DB
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    // Retrieve account by its id. Returns account obj if record with such account_id
    // exists, null otherwise
    public Account getAccountByID(int account_id) {
        Optional<Account> optionalAccount = accountRepository.findById(account_id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }


    // Retrieve account by its username. Returns account obj if record with such account_id
    // exists, null otherwise
    public Account getAccountByUsername(String accountName) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(accountName);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }


    // Add account to a DB
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
    

    // Check if account data matches with DB records
    // Returns account obj from the DB containing account_id if passwords mathces,
    // otherwise returns null
    public Account checkPasswordMatch(Account enteredAccount) {
        Account account = getAccountByUsername(enteredAccount.getUsername());
        if (account != null && enteredAccount.getPassword().equals(account.getPassword())) {
            return account;
        } else {
            return null;
        }
    }

}
