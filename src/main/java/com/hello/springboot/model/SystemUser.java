package com.hello.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class SystemUser {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer userId;

    @TableField("user_name")
    private String userName;
}
