package com.yaroslavyankov.authmicroservice.service.impl;

import com.yaroslavyankov.authmicroservice.config.TestConfig;
import com.yaroslavyankov.authmicroservice.domain.exception.ResourceNotFoundException;
import com.yaroslavyankov.authmicroservice.domain.user.Role;
import com.yaroslavyankov.authmicroservice.domain.user.User;
import com.yaroslavyankov.authmicroservice.repository.UserRepository;
import com.yaroslavyankov.authmicroservice.service.UserService;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtRequest;
import com.yaroslavyankov.authmicroservice.web.dto.auth.JwtResponse;
import com.yaroslavyankov.authmicroservice.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void login_test() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);

        Mockito.when(userService.getByUsername(username))
                .thenReturn(user);
        Mockito.when(jwtTokenProvider.createAccessToken(userId, username, roles))
                .thenReturn(accessToken);
        Mockito.when(jwtTokenProvider.createRefreshToken(userId, username))
                .thenReturn(refreshToken);

        JwtResponse response = authService.login(request);

        Mockito.verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );


        Assertions.assertEquals(username, response.getUsername());
        Assertions.assertEquals(userId, response.getId());
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername_test() {
        String username = "username";
        String password = "password";

        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        Mockito.when(userService.getByUsername(username))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(jwtTokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () ->  authService.login(request));
    }

    @Test
    void refresh_test() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newRefreshToken = "newRefreshToken";

        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);

        Mockito.when(jwtTokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(response);

        JwtResponse testResponse = authService.refresh(refreshToken);
        Mockito.verify(jwtTokenProvider).refreshUserTokens(refreshToken);

        Assertions.assertEquals(response, testResponse);
    }
}
