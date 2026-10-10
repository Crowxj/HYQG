package com.kyhorizon.hyqgsbs.mapper;

import com.kyhorizon.hyqgsbs.entity.SysAccount;
import org.apache.ibatis.annotations.Param;

public interface SysAccountMapper {
    // 根据用户名查询账号，判断是否重复
    SysAccount selectByUsername(@Param("username") String username);
    // 新增账号
    int insert(SysAccount sysAccount);
    // 新增：更新账号信息（更新last_login_time）
    int updateById(SysAccount sysAccount);
    // 重置密码专用，只更新密码
    int updatePasswordById(@Param("id") Long id, @Param("password") String password);
}
