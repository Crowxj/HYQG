package com.kyhorizon.hyqgsbs.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysAccountWhitelist {
    private Long id;
    private String username;
    private LocalDateTime createTime;
}