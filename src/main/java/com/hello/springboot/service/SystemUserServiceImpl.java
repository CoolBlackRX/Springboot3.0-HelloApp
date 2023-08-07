package com.hello.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.springboot.mapper.SystemUserMapper;
import com.hello.springboot.model.SystemUser;
import org.springframework.stereotype.Component;

@Component
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {
}
