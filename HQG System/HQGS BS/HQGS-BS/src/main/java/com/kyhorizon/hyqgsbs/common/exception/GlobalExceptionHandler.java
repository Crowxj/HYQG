package com.kyhorizon.hyqgsbs.common.exception;

import com.kyhorizon.hyqgsbs.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常捕获
 * 拦截所有@RestController抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Redis连接失败捕获
    @ExceptionHandler(RedisConnectionFailureException.class)
    public Result<?> handleRedisException(RedisConnectionFailureException e) {
        log.error("Redis连接异常", e);
        return Result.fail("缓存服务连接失败，请联系管理员");
    }

    // ========== 新增：邮件发送异常单独捕获 ==========
    @ExceptionHandler(MailSendException.class)
    public Result<?> handleMailException(MailSendException e) {
        log.error("邮件发送异常", e);
        String msg = e.getMessage();
        // 判断：收件邮箱不存在（554错误）
        if(msg.contains("554") || msg.contains("doesn't exist") || msg.contains("dosn't exist")){
            return Result.fail("该邮箱账号不存在，请确认账号已在企业邮箱后台开通");
        }
        return Result.fail("邮件发送失败，请检查邮箱配置或收件地址");
    }

    // 捕获业务抛出的 RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("业务运行异常", e);
        return Result.fail(e.getMessage());
    }

    // 兜底捕获全部Exception
    @ExceptionHandler(Exception.class)
    public Result<?> handleAllException(Exception e) {
        log.error("系统未知异常", e);
        return Result.fail("服务器内部异常，请联系管理员");
    }
}
