package com.yaroslavyankov.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class VaadinApplication {
    public static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }
}
