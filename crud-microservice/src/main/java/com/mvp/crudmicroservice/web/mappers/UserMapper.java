package com.mvp.crudmicroservice.web.mappers;

import com.mvp.crudmicroservice.domain.user.User;
import com.mvp.crudmicroservice.web.dto.user.UserDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

}
