package com.kyhorizon.hyqgsbs.config;

import com.kyhorizon.hyqgsbs.entity.SysAccountSystem;
import com.kyhorizon.hyqgsbs.mapper.SysAccountSystemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 项目启动自动初始化内置系统账号
 * CommandLineRunner：SpringBoot启动完成后自动执行run方法
 * 【修改】内置账号存入 sys_account_system 系统账号表
 */
@Component
@RequiredArgsConstructor
public class StartupDataInit implements CommandLineRunner {

    private final SysAccountSystemMapper sysAccountSystemMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 内置系统账号常量（完整邮箱，和登录接口一致）
    private static final String LEADER_ACCOUNT = "leader@kyhorizon.com";
    private static final String MAINTAINER_ACCOUNT = "maintainer@kyhorizon.com";
    private static final String INIT_PASSWORD = "admin123456";
    private static final int ACCOUNT_TYPE_SYSTEM = 0; //系统内置账号 0=系统账号
    // 角色：0普通用户，1操作员，2部门管理员，3最高权限管理员
    private static final int ROLE_LEADER = 3;
    private static final int ROLE_MAINTAINER = 4;

    @Override
    public void run(String... args) throws Exception {
        initAccount(LEADER_ACCOUNT, ROLE_LEADER);
        initAccount(MAINTAINER_ACCOUNT, ROLE_MAINTAINER);
    }

    private void initAccount(String username, Integer roleType) {
        // 查询账号是否已经存在
        SysAccountSystem exist = sysAccountSystemMapper.selectByUsername(username);
        if (exist != null) {
            System.out.println("【初始化系统账号】账号已存在，跳过：" + username);
            return;
        }
        // 账号不存在，新增到 sys_account_system
        SysAccountSystem account = new SysAccountSystem();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(INIT_PASSWORD));
        account.setRoleType(roleType);
        account.setAccountType(ACCOUNT_TYPE_SYSTEM); // 设置为系统账号 0
        account.setLastLoginTime(null);
        sysAccountSystemMapper.insert(account);
        System.out.println("【初始化系统账号】成功创建内置账号：" + username);
    }
}
