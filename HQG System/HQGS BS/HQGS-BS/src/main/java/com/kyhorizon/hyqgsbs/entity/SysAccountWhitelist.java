package com.kyhorizon.hyqgsbs.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统账号白名单表
 */
@Data
public class SysAccountWhitelist {
    private Long id;
    private String username;
    private LocalDateTime createTime;
}