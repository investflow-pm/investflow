package com.mvp.crudmicroservice.user.web.dto.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String investAccountId;

    private UserDto userDto;

    private BigDecimal balance;

}
