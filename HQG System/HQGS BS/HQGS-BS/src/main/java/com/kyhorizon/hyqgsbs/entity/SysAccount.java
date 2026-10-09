package com.kyhorizon.hyqgsbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysAccount {
    private Long id;
    private String username;
    private String password;
    private Long empId;
    private Integer roleType;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}
