package com.example.dto.mapper;

import com.example.dto.AccountDto;
import com.example.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Account toEntity(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }

    public AccountDto toDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }
}
