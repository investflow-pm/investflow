package com.mvp.investservice.web.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String username;

    private String password;

    private Set<Role> roles;

}
