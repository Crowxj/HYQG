package com.kyhorizon.hyqgsbs.common.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    // 邮箱后缀固定
    public static final String EMAIL_SUFFIX = "@kyhorizon.com";
    // ========= Redis Key前缀【按类型区分】 =========
    // 注册验证码
    public static final String REDIS_CODE_REGISTER_PREFIX = "register:email:code:";
    // 重置密码验证码
    public static final String REDIS_CODE_RESETPWD_PREFIX = "resetpwd:email:code:";

    // 验证码有效期 10分钟
    public static final long CODE_EXPIRE = 10;
    //发件人邮箱
    public static final String MAIL_FROM = "wuxiaojie@kyhorizon.com";
    /** 同一邮箱最小发送间隔(秒) */
    public static final long SEND_INTERVAL_SECONDS = 60;
    /** 同一邮箱每日最大发送次数 */
    public static final int DAILY_MAX_SEND = 20;
    /** 冷却redis key前缀 */
    public static final String REDIS_SEND_LIMIT_PREFIX = "register:send:limit:";
    /** 每日计数redis key前缀 */
    public static final String REDIS_SEND_DAILY_PREFIX = "register:send:daily:";

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送注册验证码邮件
     *
     * @param to   收件邮箱
     * @param code 验证码
     * @throws MessagingException 邮件发送异常
     */
    public void sendRegisterEmail(String to, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(MAIL_FROM);
        helper.setTo(to);
        helper.setSubject("【浩研擎工】账号注册邮箱验证");
        String content = buildRegisterEmailContent(code);
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送重置密码验证码邮件
     *
     * @param to   收件邮箱
     * @param code 验证码
     * @throws MessagingException 邮件发送异常
     */
    public void sendResetPwdEmail(String to, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(MAIL_FROM);
        helper.setTo(to);
        helper.setSubject("【浩研擎工】密码重置邮箱验证");
        String content = buildResetPwdEmailContent(code);
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    /**
     * 构建注册邮件HTML正文
     * @param code 验证码
     * @return 邮件html文本
     */
    private String buildRegisterEmailContent(String code) {
        return """
Hello! <br/><br/>
You are registering an account for Haoyan Qinggong. This is a new account creation operation. Please enter the verification code below on the registration page to complete email verification.
<br/><br/>
Verification code: <h2>%s</h2>
<br/>
This verification code will expire automatically in 10 minutes. If expired, you may return to the registration page and click "Resend Email" to get a new code.
<br/><br/>
If you did not initiate this registration, please ignore this email. Your password and account information will remain secure.
<br/><br/>
Thank you for using our service, <br/>
Haoyan Qinggong Team
""".formatted(code);
    }

    /**
     * 构建重置密码邮件HTML正文
     * @param code 验证码
     * @return 邮件html文本
     */
    private String buildResetPwdEmailContent(String code) {
        return """
Hello! <br/><br/>
You are requesting password reset for your Haoyan Qinggong account. Please enter the verification code below on the password reset page.
<br/><br/>
Verification code: <h2>%s</h2>
<br/>
This verification code will expire automatically in 10 minutes. If expired, return to the reset page and resend code.
<br/><br/>
If you did NOT request password reset, please ignore this email. Your account remains safe.
<br/><br/>
Thank you for using our service, <br/>
Haoyan Qinggong Team
""".formatted(code);
    }

    /**
     * 根据类型获取对应的redis key前缀
     * @param type register / resetPwd
     * @return redis前缀
     */
    public static String getCodeRedisPrefix(String type) {
        if ("register".equals(type)) {
            return REDIS_CODE_REGISTER_PREFIX;
        } else if ("resetPwd".equals(type)) {
            return REDIS_CODE_RESETPWD_PREFIX;
        } else {
            throw new RuntimeException("不支持的验证码类型");
        }
    }
}
