package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.AccountService;
import com.mvp.investservice.service.props.LinkProperties;
import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final InvestApi investApi;

    private final RestTemplate restTemplate;

    private final LinkProperties linkProperties;

    @Override
    public AccountDto openAccount(Long userId) {
        String accountId = investApi.getSandboxService()
                .openAccountSync();

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        AccountDto accountDto = new AccountDto();
        accountDto.setInvestAccountId(accountId);
        accountDto.setUserDto(userDto);
        accountDto.setBalance(new BigDecimal("0.0"));

        HttpEntity<AccountDto> request = new HttpEntity<>(accountDto);
        ResponseEntity<AccountDto> response
                = restTemplate.exchange(linkProperties.getCreateAccountLink(), HttpMethod.POST, request, AccountDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Account has not been created");
        }
    }

    /*
       TODO
        исправить работу метода по обновлению баланса
     */
    @Override
    public AccountDto payIn(String accountId, String amount) {
        MoneyValue moneyValue = getMoneyValue(amount);

        MoneyValue addedMoney = investApi.getSandboxService()
                .payInSync(accountId, moneyValue);

        AccountDto account = getAccount(accountId);
        BigDecimal newBalance = getNewAccountBalance(addedMoney, account);
        account.setBalance(newBalance);


        return getUpdatedAccount(accountId, account);
    }

    @Override
    public AccountDto getAccount(String accountId) {
        ResponseEntity<AccountDto> response =
                restTemplate.getForEntity(linkProperties.getAccountServiceLink() + "/{accountId}", AccountDto.class, accountId);
        return response.getBody();
    }

    private MoneyValue getMoneyValue(String amount) {
        String[] moneyValue = amount.trim().split(",");
        long rubs = Long.parseLong(moneyValue[0]);
        int cents = Integer.parseInt(moneyValue[1]);

        return MoneyValue.newBuilder()
                .setCurrency("RUB")
                .setUnits(rubs)
                .setNano(cents)
                .build();
    }

    private BigDecimal getNewAccountBalance(MoneyValue addedMoney, AccountDto account) {
        String moneyToAdd = String.format("%s.%s", addedMoney.getUnits(), addedMoney.getNano());
        return account.getBalance().add(new BigDecimal(moneyToAdd));
    }

    private AccountDto getUpdatedAccount(String accountId, AccountDto account) {

        HttpEntity<AccountDto> request = new HttpEntity<>(account);
        ResponseEntity<AccountDto> response =
                restTemplate.exchange(linkProperties.getAccountServiceLink(), HttpMethod.PUT, request, AccountDto.class);

        return response.getBody();
    }
}
