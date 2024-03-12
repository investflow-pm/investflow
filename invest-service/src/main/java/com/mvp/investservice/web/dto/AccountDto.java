package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * дтошка для создания аккаунта в инвестициях
 * и привязки его к ползователю приложения
 */

@Data
public class AccountDto {

    private String investAccountId;

    private UserDto userDto;

}
