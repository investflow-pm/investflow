package com.mvp.crudmicroservice.user.web.dto.user;

import lombok.Data;

@Data
public class TelegramDto {
    private Long id;

    private String name;

    private String telegramId;
}
