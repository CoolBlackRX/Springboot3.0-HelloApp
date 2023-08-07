package com.hello.springboot;

import com.hello.springboot.model.SystemUser;
import com.hello.springboot.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles({"dev", "personal"})
@SpringBootTest(classes = HelloSpringBoot3XApplicationRunner.class)
public class SystemUserMybatisPlusTest {
    private SystemUserService systemUserService;

    @Autowired
    public void setSystemUserService(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @Test
    void test() {
        SystemUser systemUser = systemUserService.getById(1);
        log.info("查询到的用户:{}", systemUser);
    }
}
