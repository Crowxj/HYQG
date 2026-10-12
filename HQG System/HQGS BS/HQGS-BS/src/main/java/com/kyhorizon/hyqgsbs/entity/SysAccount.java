package com.kyhorizon.hyqgsbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统注册账号表
 */
@Data
public class SysAccount {
    private Long id;
    private String username;//账号
    private String password;//密码
    private Long empId;//员工ID
    private Integer roleType;//账号权限
    private Integer status;//账号状态
    private Integer accountType;//账号类型
    private LocalDateTime lastLoginTime;//最近一次登录
    private LocalDateTime createTime;//创建时间
}
