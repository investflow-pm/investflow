package com.mvp.investservice.web.dto;

import com.mvp.investservice.web.dto.user.UserDto;
import lombok.Data;

/**
 * дтошка для создания аккаунта в инвестициях
 * и привязки его к ползователю приложения
 */

@Data
public class AccountDto {

    private String investAccountId;

    private UserDto userDto;

}
