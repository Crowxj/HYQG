package com.kyhorizon.hyqgsbs.mapper;

import com.kyhorizon.hyqgsbs.entity.SysAccountSystem;
import org.apache.ibatis.annotations.Param;

public interface SysAccountSystemMapper {
    // 根据用户名查询账号，判断是否重复
    SysAccountSystem selectByUsername(@Param("username") String username);
    // 新增系统账号
    int insert(SysAccountSystem sysAccountSystem);
    // 新增：更新系统账号的last_login_time
    int updateById(SysAccountSystem sysAccountSystem);
}
