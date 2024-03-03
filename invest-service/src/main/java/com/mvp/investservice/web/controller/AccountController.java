package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.AccountService;
import com.mvp.investservice.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.Account;

@RestController
@RequestMapping("api/v1/invest/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public AccountDto openAccount(@PathVariable Long userId) {
        return accountService.openAccount(userId);
    }

    @PutMapping("/{accountId}")
    public AccountDto payIn(@PathVariable String accountId,
                            @RequestParam(value = "amount") String amount) {
        return accountService.payIn(accountId, amount);
    }
}
