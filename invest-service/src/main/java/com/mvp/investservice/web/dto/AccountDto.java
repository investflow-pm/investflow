package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String investAccountId;

    private UserDto userDto;

    private BigDecimal balance;

}
