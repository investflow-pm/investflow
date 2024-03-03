package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.UserDto;

public interface AccountService {

    AccountDto openAccount(Long userId);

    AccountDto getAccount(String accountId);

    AccountDto payIn(String accountId, String amount);
}
