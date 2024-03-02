package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.AccountService;
import com.mvp.investservice.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/invest/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public UserDto openAccount(@PathVariable Long userId) {
        return accountService.openAccount(userId);
    }
}
