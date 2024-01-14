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
@RequestMapping("/register")
@PropertySource("classpath:messages.properties")
public class RegisterAccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Value("${error.register.user.exist}")
    private String userExistMsg;
    @Value("${error.register.invalid}")
    private String invalidMsg;
    @Value("${info.register.create.successfully}")
    private String createSuccessfullyMsg;

    public RegisterAccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<AccountDto> registerAccount(@RequestBody AccountDto accountDto) {
        Account entity = accountMapper.toEntity(accountDto);
        Optional<Account> account = accountService.getAccountByUsername(entity.getUsername());
        if (account.isPresent()) {
            log.error(userExistMsg);

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (entity.getUsername().isEmpty() && entity.getPassword().length() < 4) {
            log.error(invalidMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        accountService.create(entity);
        log.info(createSuccessfullyMsg);

        return ResponseEntity.ok().build();
    }
}
