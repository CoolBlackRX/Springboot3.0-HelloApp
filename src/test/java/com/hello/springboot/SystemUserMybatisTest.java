package com.hello.springboot;

import com.hello.springboot.mapper.SystemUserMapper;
import com.hello.springboot.model.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;


@Slf4j
@ActiveProfiles({"dev", "personal", "test"})
@MybatisTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SystemUserMybatisTest {
    private SystemUserMapper systemUserMapper;

    @Autowired
    public void setSystemUserMapper(SystemUserMapper systemUserMapper) {
        this.systemUserMapper = systemUserMapper;
    }


    @Test
    void test() {
        SystemUser systemUser = systemUserMapper.getByUserId(1);
        log.info("查询到的用户:{}", systemUser);
    }

}
