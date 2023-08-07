package com.hello.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.springboot.model.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {
    SystemUser getByUserId(@Param("userId") Integer userId);
}
