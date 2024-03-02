package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.domain.exception.UpdateException;
import com.mvp.investservice.service.AccountService;
import com.mvp.investservice.service.props.LinkProperties;
import com.mvp.investservice.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.piapi.core.InvestApi;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final InvestApi investApi;

    private final RestTemplate restTemplate;

    private final LinkProperties linkProperties;

    @Override
    public UserDto openAccount(Long userId) {
        String accountId = investApi.getSandboxService()
                .openAccountSync();

        UserDto userDto = getUserById(userId, accountId);
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<UserDto> response
                = restTemplate.exchange(linkProperties.getUserServiceLink(), HttpMethod.PUT, request, UserDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UpdateException("Update user info exception");
        }
    }

    private UserDto getUserById(Long userId, String accountId) {
        ResponseEntity<UserDto> response
                = restTemplate.getForEntity(linkProperties.getUserServiceLink() + "/{userId}", UserDto.class, userId);
        if (response.getStatusCode().is2xxSuccessful()) {
            UserDto userDto = response.getBody();
            if (userDto.getInvestAccountId() == null) {
                userDto.setInvestAccountId(accountId);
            }

            return userDto;
        } else {
            throw new ResourceNotFoundException("Пользователя с таким id не найдено");
        }
    }
}
