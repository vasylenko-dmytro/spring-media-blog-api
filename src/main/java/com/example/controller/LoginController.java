package com.example.controller;

import com.example.dto.AccountDto;
import com.example.dto.mapper.AccountMapper;
import com.example.entity.Account;
import com.example.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/login")
@PropertySource("classpath:messages.properties")
public class LoginController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Value("${info.login.successfully}")
    private String loginSuccessfullyMsg;
    @Value("${error.login.invalid}")
    private String loginInvalidMsg;

    public LoginController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<AccountDto> login(@RequestBody AccountDto accountDto) {
        Account entity = accountMapper.toEntity(accountDto);
        Optional<Account> account = accountService.getAccountByUsername(entity.getUsername());
        if (account.isPresent()
                && account.get().getUsername().equals(entity.getUsername())
                && account.get().getPassword().equals(entity.getPassword())) {
            log.info(loginSuccessfullyMsg);

            return ResponseEntity.ok(accountMapper.toDto(account.get()));
        }
        log.error(loginInvalidMsg);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
