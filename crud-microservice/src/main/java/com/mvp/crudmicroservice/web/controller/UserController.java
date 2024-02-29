package com.mvp.crudmicroservice.web.controller;

import com.mvp.crudmicroservice.domain.exception.ResourceNotFoundException;
import com.mvp.crudmicroservice.domain.exception.UserAlreadyExistsException;
import com.mvp.crudmicroservice.domain.user.User;
import com.mvp.crudmicroservice.service.UserService;
import com.mvp.crudmicroservice.web.dto.auth.JwtRequest;
import com.mvp.crudmicroservice.web.dto.user.UserDto;
import com.mvp.crudmicroservice.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crud/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userMapper.toEntity(userDto);
            User createdUser = userService.create(user);

            return ResponseEntity.ok().body(userMapper.toDto(createdUser));
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> getUser(@RequestBody JwtRequest jwtRequest) {
        try {
            User user = userService.getByUsername(jwtRequest.getUsername());
            return ResponseEntity.ok().body(userMapper.toDto(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        if (updatedUser != null) {
            return ResponseEntity.ok(userMapper.toDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            return ResponseEntity.ok().body(userMapper.toDto(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam(value = "username") String username) {
        try {
            User user = userService.getByUsername(username);
            return ResponseEntity.ok().body(userMapper.toDto(user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().body("Пользователь успешно удалён");
    }
}