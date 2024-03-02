package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.Account;
import com.mvp.investservice.web.dto.UserDto;

public interface AccountService {

    UserDto openAccount(Long userId);
}
