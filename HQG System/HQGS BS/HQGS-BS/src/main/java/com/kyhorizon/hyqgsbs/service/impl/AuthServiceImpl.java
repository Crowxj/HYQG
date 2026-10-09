package com.kyhorizon.hyqgsbs.service.impl;

import com.kyhorizon.hyqgsbs.common.util.CodeUtil;
import com.kyhorizon.hyqgsbs.common.util.EmailUtil;
import com.kyhorizon.hyqgsbs.common.util.JwtUtil;
import com.kyhorizon.hyqgsbs.entity.SysAccount;
import com.kyhorizon.hyqgsbs.mapper.SysAccountMapper;
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
    private EmailUtil emailUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 发送邮箱验证码
     */
    @Override
    public void sendEmailCode(Map<String, Object> map) {
        String username = (String) map.get("username");
        if (username == null || username.isBlank()) {
            throw new RuntimeException("账号不能为空");
        }

        // 先拼出完整邮箱
        String email = username + EmailUtil.EMAIL_SUFFIX;

        // ==========用完整邮箱去数据库查 ==========
        SysAccount existAccount = sysAccountMapper.selectByUsername(email);
        if (existAccount != null) {
            throw new RuntimeException("该账号已经注册，无法获取验证码");
        }

        // ===================== 1. 60秒冷却校验 =====================
        String limitKey = EmailUtil.REDIS_SEND_LIMIT_PREFIX + email;
        Boolean isCooling = stringRedisTemplate.hasKey(limitKey);
        if (Boolean.TRUE.equals(isCooling)) {
            throw new RuntimeException("邮件发送过于频繁，请" + EmailUtil.SEND_INTERVAL_SECONDS + "秒后再试");
        }

        // ===================== 2. 每日发送次数校验=====================
        String dailyKey = EmailUtil.REDIS_SEND_DAILY_PREFIX + email;
        String countStr = stringRedisTemplate.opsForValue().get(dailyKey);
        Long todayCount = countStr == null ? 0L : Long.parseLong(countStr);

        if (todayCount >= EmailUtil.DAILY_MAX_SEND) {
            throw new RuntimeException("今日验证码发送次数已达上限，请明天再试");
        }
        todayCount = stringRedisTemplate.opsForValue().increment(dailyKey, 1);
        if (todayCount == 1) {
            stringRedisTemplate.expireAt(dailyKey, getEndOfToday());
        }

        // 生成验证码
        String code = CodeUtil.generateSixDigitCode();
        String redisKey = EmailUtil.REDIS_CODE_PREFIX + email;
        stringRedisTemplate.opsForValue().set(redisKey, code, EmailUtil.CODE_EXPIRE, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(limitKey, "1", EmailUtil.SEND_INTERVAL_SECONDS, TimeUnit.SECONDS);

        // 发送邮件
        try {
            emailUtil.sendRegisterEmail(email, code);
        } catch (MessagingException e) {
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
     * 校验验证码
     */
    @Override
    public boolean checkEmailCode(Map<String, Object> map) {
        String username = (String) map.get("username");
        String inputCode = (String) map.get("code");
        if (username == null || inputCode == null) {
            return false;
        }
        String email = username + EmailUtil.EMAIL_SUFFIX;
        String redisKey = EmailUtil.REDIS_CODE_PREFIX + email;
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
            throw new RuntimeException("参数缺失");
        }
        // 拼完整邮箱
        String email = username + EmailUtil.EMAIL_SUFFIX;
        String redisKey = EmailUtil.REDIS_CODE_PREFIX + email;
        //再次校验验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (cacheCode == null || !cacheCode.equals(inputCode)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        //密码强度校验
        String pwdPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!password.matches(pwdPattern)) {
            throw new RuntimeException("密码至少8个字符，必须包含字母和数字，不能包含空格");
        }
        // 数据库注册事务
        doRegisterDb(email, password);

        //注册成功，清理缓存
        stringRedisTemplate.delete(redisKey);
        stringRedisTemplate.delete(EmailUtil.REDIS_SEND_LIMIT_PREFIX + email);
        stringRedisTemplate.delete(EmailUtil.REDIS_SEND_DAILY_PREFIX + email);
    }

    /**
     * 数据库操作事务方法
     */
    private void doRegisterDb(String email, String password) {
        //2.判断账号是否重复（用完整邮箱查）
        SysAccount exist = sysAccountMapper.selectByUsername(email);
        if (exist != null) {
            throw new RuntimeException("账号已存在");
        }

        //3.密码BCrypt加密
        String encryptPwd = bCryptPasswordEncoder.encode(password);

        //4.插入数据库，存完整邮箱
        SysAccount account = new SysAccount();
        account.setUsername(email);
        account.setPassword(encryptPwd);
        account.setEmpId(null);
        account.setRoleType(1);
        account.setStatus(1);
        account.setCreateTime(LocalDateTime.now());
        account.setLastLoginTime(null);
        sysAccountMapper.insert(account);
    }

    /**
     * ✅ 修改登录方法，使用Security AuthenticationManager认证
     */
    @Override
    public String login(Map<String, Object> map) {
        String username = (String) map.get("username");
        String rawPwd = (String) map.get("password");
        if(username == null || rawPwd == null){
            throw new RuntimeException("账号密码不能为空");
        }
        // 使用Security进行账号密码校验
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPwd)
        );
        //认证成功，更新登录时间
        SysAccount account = sysAccountMapper.selectByUsername(username);
        account.setLastLoginTime(LocalDateTime.now());
        sysAccountMapper.updateById(account);
        // 使用JwtUtil生成token
        return jwtUtil.generateToken(username);
    }
}
