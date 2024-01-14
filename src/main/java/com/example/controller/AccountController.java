package com.example.controller;

import com.example.dto.AccountDto;
import com.example.dto.MessageDto;
import com.example.dto.mapper.AccountMapper;
import com.example.dto.mapper.MessageMapper;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final MessageService messageService;
    private final AccountMapper accountMapper;
    private final MessageMapper messageMapper;

    public AccountController(AccountService accountService, MessageService messageService,
                             AccountMapper accountMapper, MessageMapper messageMapper) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.accountMapper = accountMapper;
        this.messageMapper = messageMapper;
    }

    @GetMapping()
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/messages")
    @ResponseBody
    public ResponseEntity<List<MessageDto>> findMessagesByAccountId(@PathVariable Integer id) {
        List<Message> messages = messageService.getAllAccountMessages(id);
        return ResponseEntity.ok(messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList()));
    }
}
