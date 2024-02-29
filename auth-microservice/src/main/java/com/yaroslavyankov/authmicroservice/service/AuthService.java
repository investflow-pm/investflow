package com.yaroslavyankov.authmicroservice.service;

import com.yaroslavyankov.authmicroservice.web.dto.UserDto;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtRequest;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

    UserDto register(UserDto userDto);
}
