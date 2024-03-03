package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.domain.exception.UpdateException;
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
                = restTemplate.exchange(linkProperties.getAccountServiceLink(), HttpMethod.POST, request, AccountDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Account has not been created");
        }
    }
}
