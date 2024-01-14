package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public void create(Account account) {
        repository.save(account);
    }

    public Optional<Account> getAccountById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }
}
