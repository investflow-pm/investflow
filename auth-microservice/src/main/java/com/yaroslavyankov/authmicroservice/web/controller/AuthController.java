package com.yaroslavyankov.authmicroservice.web.controller;

import com.yaroslavyankov.authmicroservice.domain.user.User;
import com.yaroslavyankov.authmicroservice.service.AuthService;
import com.yaroslavyankov.authmicroservice.service.UserService;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtRequest;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtResponse;
import com.yaroslavyankov.authmicroservice.web.dto.user.UserDto;
import com.yaroslavyankov.authmicroservice.web.dto.validation.OnCreate;
import com.yaroslavyankov.authmicroservice.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated
                             @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class)
                            @RequestBody final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
