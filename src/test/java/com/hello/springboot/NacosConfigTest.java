package com.hello.springboot;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles({"dev", "personal"})
@SpringBootTest(classes = HelloSpringBoot3XApplicationRunner.class)
public class NacosConfigTest implements EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Test
    void test() {
        String employeeName = environment.getProperty("employee.name");
        String employeeAge = environment.getProperty("employee.age");
        log.info("员工名称和年龄：{} --- {}", employeeName, employeeAge);
    }
}
