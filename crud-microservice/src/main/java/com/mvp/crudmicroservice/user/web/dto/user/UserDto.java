package com.mvp.crudmicroservice.user.web.dto.user;

import com.mvp.crudmicroservice.user.domain.user.Role;
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
