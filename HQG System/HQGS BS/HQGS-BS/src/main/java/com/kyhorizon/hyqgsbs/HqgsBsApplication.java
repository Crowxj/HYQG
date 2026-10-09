package com.kyhorizon.hyqgsbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 重点！扫描mapper所在包
@MapperScan("com.kyhorizon.hyqgsbs.mapper")
public class HqgsBsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HqgsBsApplication.class, args);
    }
}
