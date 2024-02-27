package com.yaroslavyankov.authmicroservice.web.mappers;

import com.yaroslavyankov.authmicroservice.domain.user.User;
import com.yaroslavyankov.authmicroservice.web.dto.user.UserDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

}
