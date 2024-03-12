package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.PayInDto;
import com.mvp.investservice.web.dto.AccountDto;
import ru.tinkoff.piapi.contract.v1.MoneyValue;

public interface AccountService {

    AccountDto openAccount(Long userId);

    AccountDto getAccount();

    MoneyValue payIn(PayInDto payInDto);
}
