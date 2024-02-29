package com.mvp.crudmicroservice.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvp.crudmicroservice.domain.user.Role;
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
