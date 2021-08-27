package com.test.codediff;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.test.codediff.mapper")
public class CodeDiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeDiffApplication.class, args);
    }

}
