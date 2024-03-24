package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.AccountService;
import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.BalanceDto;
import com.mvp.investservice.web.dto.PayInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/invest/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public AccountDto openAccount(@PathVariable Long userId) {
        return accountService.openAccount(userId);
    }

    @PutMapping()
    public BalanceDto payIn(@RequestBody PayInDto payInDto) {
        return accountService.payIn(payInDto);
    }

    @GetMapping
    public AccountDto getAccount() {
        return accountService.getAccount();
    }
}
