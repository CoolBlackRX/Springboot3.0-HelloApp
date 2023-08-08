package com.hello.springboot;

import com.hello.springboot.model.SystemUser;
import com.hello.springboot.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    @Test
    void test2() {
        List<SystemUser> users = systemUserService.list();
        for (SystemUser user : users) {
            log.info("查询到的用户:{}", user);
        }

        SystemUser user = new SystemUser();
        user.setUserName("测试用户");
        boolean save = systemUserService.save(user);
        log.info("插入用户成功: {}", save);
        // 再读一次，数据库没开主从同步，执行完检查下主库是否写入数据
        List<SystemUser> users2 = systemUserService.list();
        for (SystemUser user2 : users2) {
            log.info("查询到的用户:{}", user2);
        }
    }
}
