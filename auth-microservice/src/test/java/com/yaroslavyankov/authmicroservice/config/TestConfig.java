package com.yaroslavyankov.authmicroservice.config;

import com.yaroslavyankov.authmicroservice.repository.UserRepository;
import com.yaroslavyankov.authmicroservice.service.AuthService;
import com.yaroslavyankov.authmicroservice.service.UserService;
import com.yaroslavyankov.authmicroservice.service.impl.AuthServiceImpl;
import com.yaroslavyankov.authmicroservice.service.impl.UserServiceImpl;
import com.yaroslavyankov.authmicroservice.service.props.JwtProperties;
import com.yaroslavyankov.authmicroservice.web.security.JwtTokenProvider;
import com.yaroslavyankov.authmicroservice.web.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "YXNkZmFqc2Rma2pma3drZWZxandsZmtzZGFqaw==");
        return jwtProperties;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService(userService());
    }

    @Bean
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(jwtProperties(), userDetailsService(), userService());
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, testPasswordEncoder());
    }

    @Bean
    @Primary
    public AuthService authService(){
        return new AuthServiceImpl(authenticationManager, userService(), tokenProvider());
    }
}
