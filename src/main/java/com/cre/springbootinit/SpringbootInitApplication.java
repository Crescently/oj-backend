package com.cre.springbootinit;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.cre.springbootinit.mapper")
@Slf4j
public class SpringbootInitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootInitApplication.class, args);
        log.info("后端接口文档：" + "http://localhost:8081/doc.html#/home");
        log.info("OPENAPI 文档：" + "http://localhost:8081/v3/api-docs");
    }

}
