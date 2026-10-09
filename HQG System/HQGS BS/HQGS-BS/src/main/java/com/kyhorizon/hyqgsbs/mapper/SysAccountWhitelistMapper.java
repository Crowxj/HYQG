package com.kyhorizon.hyqgsbs.mapper;

import com.kyhorizon.hyqgsbs.entity.SysAccountWhitelist;
import org.apache.ibatis.annotations.Param;

public interface SysAccountWhitelistMapper {
    // 根据完整邮箱查询白名单记录
    SysAccountWhitelist selectByUsername(@Param("username") String username);
}
