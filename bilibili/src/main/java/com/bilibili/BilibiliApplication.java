package com.bilibili;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.bilibili.mapper")
@EnableScheduling
public class BilibiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliApplication.class, args);
    }

}
