package com.kyhorizon.hyqgsbs.config;

import com.kyhorizon.hyqgsbs.entity.SysAccount;
import com.kyhorizon.hyqgsbs.mapper.SysAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * 项目启动自动初始化内置系统账号
 * CommandLineRunner：SpringBoot启动完成后自动执行run方法
 */
@Component
@RequiredArgsConstructor
public class StartupDataInit implements CommandLineRunner {

    private final SysAccountMapper sysAccountMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 内置系统账号常量（完整邮箱，和登录接口一致）
    private static final String LEADER_ACCOUNT = "leader@kyhorizon.com";
    private static final String MAINTAINER_ACCOUNT = "maintainer@kyhorizon.com";
    private static final String INIT_PASSWORD = "admin123456";
    private static final int ACCOUNT_TYPE_SYSTEM = 0; //系统内置账号
    private static final int ACCOUNT_STATUS_ENABLE = 1;

    @Override
    public void run(String... args) throws Exception {
        initAccount(LEADER_ACCOUNT);
        initAccount(MAINTAINER_ACCOUNT);
    }

    private void initAccount(String username) {
        // 查询账号是否已经存在
        SysAccount exist = sysAccountMapper.selectByUsername(username);
        if (exist != null) {
            System.out.println("【初始化账号】账号已存在，跳过：" + username);
            return;
        }
        // 账号不存在，新增
        SysAccount account = new SysAccount();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(INIT_PASSWORD));
        account.setEmpId(null);
        account.setRoleType(1);
        account.setStatus(ACCOUNT_STATUS_ENABLE); // 1=启用
        account.setAccountType(ACCOUNT_TYPE_SYSTEM); // 设置为系统账号 0
        account.setCreateTime(LocalDateTime.now());
        account.setLastLoginTime(null);
        sysAccountMapper.insert(account);
        System.out.println("【初始化账号】成功创建内置账号：" + username);
    }
}
