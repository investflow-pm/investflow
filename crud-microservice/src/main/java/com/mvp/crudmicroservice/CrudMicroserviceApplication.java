package com.mvp.crudmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CrudMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudMicroserviceApplication.class, args);
    }

}
