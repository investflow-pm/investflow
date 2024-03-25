package com.mvp.crudmicroservice.user.web.controller;

import com.mvp.crudmicroservice.user.domain.exception.ResourceNotFoundException;
import com.mvp.crudmicroservice.user.domain.user.Account;
import com.mvp.crudmicroservice.user.service.AccountService;
import com.mvp.crudmicroservice.user.web.dto.user.AccountDto;
import com.mvp.crudmicroservice.user.web.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crud/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @PutMapping
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        Account updatedAccount = accountService.update(account);

        return ResponseEntity
                .ok()
                .body(accountMapper.toDto(account));
    }


    @GetMapping("/{userId}")
    public ResponseEntity getAccountByUserId(@PathVariable Long userId) {
        try {
            Account account = accountService.getByUserId(userId);
            return ResponseEntity
                    .ok()
                    .body(accountMapper.toDto(account));
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .header("error-message", exception.getMessage())
                    .body(exception.getMessage());
        }
    }

}
