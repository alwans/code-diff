package com.test.diff.services;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.test.diff.services.mapper")
public class DiffServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffServicesApplication.class, args);
    }

}
