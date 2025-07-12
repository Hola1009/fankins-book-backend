package com.fankinsbook.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fankinsbook.auth.domain.mapper")
public class FankinsBookAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FankinsBookAuthApplication.class, args);
    }

}
