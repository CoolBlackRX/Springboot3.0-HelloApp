package com.hello.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class HelloSpringBoot3XApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(HelloSpringBoot3XApplicationRunner.class, args);
        log.info("服务启动成功");
    }
}
