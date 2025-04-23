package com.cre.oj;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "com.cre.oj.mapper")
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpringbootInitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootInitApplication.class, args);
        log.info("后端接口文档：" + "http://localhost:8081/doc.html#/home");
        log.info("OPENAPI 文档：" + "http://localhost:8081/v3/api-docs");
    }

}
