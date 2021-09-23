package com.test.diff.services;

import com.test.diff.services.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.test.diff.services.mapper")
public class DiffServicesApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(DiffServicesApplication.class, args);
        SpringUtil.setContext(app);
    }
}
