package com.kyhorizon.hyqgsbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统账号表
 */
@Data
public class SysAccountSystem {
    private Long id;
    private String username;
    private String password;
    private Integer roleType;
    private Integer accountType;
    private LocalDateTime lastLoginTime;
}
