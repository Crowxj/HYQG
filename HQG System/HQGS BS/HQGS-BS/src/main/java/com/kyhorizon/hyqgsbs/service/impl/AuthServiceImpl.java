package com.kyhorizon.hyqgsbs.service.impl;

import com.kyhorizon.hyqgsbs.common.exception.BusinessException;
import com.kyhorizon.hyqgsbs.common.util.CodeUtil;
import com.kyhorizon.hyqgsbs.common.util.EmailUtil;
import com.kyhorizon.hyqgsbs.common.util.JwtUtil;
import com.kyhorizon.hyqgsbs.entity.SysAccount;
import com.kyhorizon.hyqgsbs.entity.SysAccountSystem;
import com.kyhorizon.hyqgsbs.entity.SysAccountWhitelist;
import com.kyhorizon.hyqgsbs.mapper.SysAccountMapper;
import com.kyhorizon.hyqgsbs.mapper.SysAccountSystemMapper;
import com.kyhorizon.hyqgsbs.mapper.SysAccountWhitelistMapper;
import com.kyhorizon.hyqgsbs.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private SysAccountSystemMapper sysAccountSystemMapper;

    @Autowired
    private SysAccountWhitelistMapper sysAccountWhitelistMapper;

    @Autowired
    private EmailUtil emailUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{2,17}$";
    private static final String USERNAME_ERR_MSG = "账号格式错误：3~18字符，字母开头，仅字母数字下划线";

    /**
     * 发送邮箱验证码，新增 type 参数区分 register / resetPwd
     */
    @Override
    public void sendEmailCode(Map<String, Object> map) {
        String username = (String) map.get("username");
        String type = (String) map.get("type");
        if (username == null || username.isBlank() || type == null || type.isBlank()) {
            throw new BusinessException(400, "参数缺失：username、type不能为空");
        }

        // ✅【新增】账号格式校验（注册、重置密码获取验证码时就拦截非法账号）
        if (!username.matches(USERNAME_PATTERN)) {
            throw new BusinessException(400, USERNAME_ERR_MSG);
        }

        String email = username + EmailUtil.EMAIL_SUFFIX;
        // 【分支逻辑】注册 / 重置密码 前置校验不一样
        if ("register".equals(type)) {
            // 注册场景：校验白名单 + 账号不能已存在
            SysAccountWhitelist whitelist = sysAccountWhitelistMapper.selectByUsername(email);
            if (whitelist == null) {
                throw new BusinessException(403, email + " 不在注册白名单，无法注册");
            }
            SysAccount existAccount = sysAccountMapper.selectByUsername(email);
            if (existAccount != null) {
                throw new BusinessException(409, email + "已经注册，无法获取验证码");
            }
            // 注册时同时校验系统账号表，防止用户名冲突
            SysAccountSystem existSysAccount = sysAccountSystemMapper.selectByUsername(email);
            if(existSysAccount != null){
                throw new BusinessException(409, email + "账号已存在，无法注册");
            }
        } else if ("resetPwd".equals(type)) {
            // 重置密码场景：只允许普通注册账号重置，系统内置账号不开放重置密码
            SysAccount existAccount = sysAccountMapper.selectByUsername(email);
            if (existAccount == null) {
                throw new BusinessException(404, "账号名称错误，无法发送重置验证码");
            }
        } else {
            throw new BusinessException(400, "type类型错误，仅支持 register / resetPwd");
        }

        // 发送频率限流（注册和重置共用同一套冷却计数）
        String limitKey = EmailUtil.REDIS_SEND_LIMIT_PREFIX + email;
        Boolean isCooling = stringRedisTemplate.hasKey(limitKey);
        if (Boolean.TRUE.equals(isCooling)) {
            throw new BusinessException(400, "邮件发送过于频繁，请" + EmailUtil.SEND_INTERVAL_SECONDS + "秒后再试");
        }

        String dailyKey = EmailUtil.REDIS_SEND_DAILY_PREFIX + email;
        String countStr = stringRedisTemplate.opsForValue().get(dailyKey);
        Long todayCount = countStr == null ? 0L : Long.parseLong(countStr);
        if (todayCount >= EmailUtil.DAILY_MAX_SEND) {
            throw new BusinessException(400, "今日验证码发送次数已达上限，请明天再试");
        }
        todayCount = stringRedisTemplate.opsForValue().increment(dailyKey, 1);
        if (todayCount == 1) {
            stringRedisTemplate.expireAt(dailyKey, getEndOfToday());
        }

        // 获取对应类型的redis前缀
        String redisCodePrefix = EmailUtil.getCodeRedisPrefix(type);
        String code = CodeUtil.generateSixDigitCode();
        String redisKey = redisCodePrefix + email;
        stringRedisTemplate.opsForValue().set(redisKey, code, EmailUtil.CODE_EXPIRE, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(limitKey, "1", EmailUtil.SEND_INTERVAL_SECONDS, TimeUnit.SECONDS);

        // 根据type发送不同邮件模板
        try {
            if ("register".equals(type)) {
                emailUtil.sendRegisterEmail(email, code);
            } else if ("resetPwd".equals(type)) {
                emailUtil.sendResetPwdEmail(email, code);
            }
        } catch (MessagingException e) {
            // 邮件底层异常，保持原有RuntimeException，交给全局兜底Exception捕获
            throw new RuntimeException("邮件发送异常", e);
        }
    }

    private Date getEndOfToday() {
        LocalDateTime endOfDay = LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 校验验证码，增加type区分
     */
    @Override
    public boolean checkEmailCode(Map<String, Object> map) {
        String username = (String) map.get("username");
        String inputCode = (String) map.get("code");
        String type = (String) map.get("type");
        if (username == null || inputCode == null || type == null) {
            return false;
        }
        if (!username.matches(USERNAME_PATTERN)) {
            return false;
        }
        String email = username + EmailUtil.EMAIL_SUFFIX;
        String redisCodePrefix = EmailUtil.getCodeRedisPrefix(type);
        String redisKey = redisCodePrefix + email;
        String cacheCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (cacheCode == null || !cacheCode.equals(inputCode)) {
            return false;
        }
        return true;
    }

    /**
     * 注册
     */
    @Override
    @Transactional
    public void register(Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String inputCode = (String) map.get("code");
        if (username == null || password == null || inputCode == null) {
            throw new BusinessException(400, "参数缺失");
        }
        // ✅【新增】注册提交账号校验
        if (!username.matches(USERNAME_PATTERN)) {
            throw new BusinessException(400, USERNAME_ERR_MSG);
        }
        String email = username + EmailUtil.EMAIL_SUFFIX;
        String redisKey = EmailUtil.REDIS_CODE_REGISTER_PREFIX + email;
        String cacheCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (cacheCode == null || !cacheCode.equals(inputCode)) {
            throw new BusinessException(400, "验证码错误或已过期");
        }
        //密码强度校验
        String pwdPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!password.matches(pwdPattern)) {
            throw new BusinessException(400, "密码至少8个字符，必须包含字母和数字，不能包含空格");
        }
        doRegisterDb(email, password);
        //注册成功清理缓存
        stringRedisTemplate.delete(redisKey);
        stringRedisTemplate.delete(EmailUtil.REDIS_SEND_LIMIT_PREFIX + email);
        stringRedisTemplate.delete(EmailUtil.REDIS_SEND_DAILY_PREFIX + email);
    }

    private void doRegisterDb(String email, String password) {
        SysAccount exist = sysAccountMapper.selectByUsername(email);
        if (exist != null) {
            throw new BusinessException(409, "账号已存在");
        }
        // 注册前二次校验系统账号表，防止重名
        SysAccountSystem existSys = sysAccountSystemMapper.selectByUsername(email);
        if(existSys != null){
            throw new BusinessException(409, "账号已存在");
        }
        String encryptPwd = bCryptPasswordEncoder.encode(password);
        SysAccount account = new SysAccount();
        account.setUsername(email);
        account.setPassword(encryptPwd);
        account.setEmpId(null);
        account.setRoleType(0);
        account.setStatus(1);
        account.setAccountType(1);
        account.setCreateTime(LocalDateTime.now());
        account.setLastLoginTime(null);
        sysAccountMapper.insert(account);
    }

    /**
     * 【新增】重置密码业务
     */
    @Override
    @Transactional
    public void resetPassword(Map<String, Object> map) {
        String username = (String) map.get("username");
        String newPassword = (String) map.get("password");
        String inputCode = (String) map.get("code");
        if (username == null || newPassword == null || inputCode == null) {
            throw new BusinessException(400, "参数缺失");
        }
        // ✅【新增】重置密码提交账号校验
        if (!username.matches(USERNAME_PATTERN)) {
            throw new BusinessException(400, USERNAME_ERR_MSG);
        }
        String email = username + EmailUtil.EMAIL_SUFFIX;
        String redisKey = EmailUtil.REDIS_CODE_RESETPWD_PREFIX + email;
        String cacheCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (cacheCode == null || !cacheCode.equals(inputCode)) {
            throw new BusinessException(400, "验证码错误或已过期");
        }
        //密码强度校验
        String pwdPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!newPassword.matches(pwdPattern)) {
            throw new BusinessException(400, "密码至少8个字符，必须包含字母和数字，不能包含空格");
        }
        SysAccount account = sysAccountMapper.selectByUsername(email);
        if (account == null) {
            throw new BusinessException(404, "账号不存在");
        }

        // ========== 新增：校验新密码不能和原有密码一致 ==========
        boolean isSamePwd = bCryptPasswordEncoder.matches(newPassword, account.getPassword());
        if (isSamePwd) {
            throw new BusinessException(409, "新密码不能与原密码相同，请更换密码");
        }

        //更新密码
        String encryptPwd = bCryptPasswordEncoder.encode(newPassword);
        account.setPassword(encryptPwd);
        sysAccountMapper.updatePasswordById(account.getId(), encryptPwd);
        //清除重置验证码缓存
        stringRedisTemplate.delete(redisKey);
    }

    /**
     * 登录【核心修改】支持两张表账号登录
     */
    @Override
    public String login(Map<String, Object> map) {
        String fullEmail = (String) map.get("username");
        String rawPwd = (String) map.get("password");
        if (fullEmail == null || rawPwd == null) {
            throw new BusinessException(400, "账号密码不能为空");
        }
        // 剥离邮箱后缀，提取纯账号名称用于正则校验
        String username = fullEmail.contains("@") ? fullEmail.split("@")[0] : fullEmail;
        if (!username.matches(USERNAME_PATTERN)) {
            throw new BusinessException(400, USERNAME_ERR_MSG);
        }

        // 1. 先执行SpringSecurity认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(fullEmail, rawPwd)
        );
        String loginName = authentication.getName();

        // 2. 异步更新登录时间：优先查sys_account，没有则查sys_account_system
        new Thread(() -> {
            try {
                SysAccount normalAccount = sysAccountMapper.selectByUsername(loginName);
                if (normalAccount != null) {
                    normalAccount.setLastLoginTime(LocalDateTime.now());
                    sysAccountMapper.updateById(normalAccount);
                } else {
                    SysAccountSystem sysAccount = sysAccountSystemMapper.selectByUsername(loginName);
                    if (sysAccount != null) {
                        sysAccount.setLastLoginTime(LocalDateTime.now());
                        sysAccountSystemMapper.updateById(sysAccount);
                    }
                }
            } catch (Exception e) {
                // 异步更新失败，只打日志，不影响登录
                e.printStackTrace();
            }
        }).start();

        return jwtUtil.generateToken(fullEmail);
    }
}
