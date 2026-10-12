package com.kyhorizon.hyqgsbs.service;

import java.util.Map;

public interface AuthService {
    void sendEmailCode(Map<String, Object> map);//发送邮箱验证码
    boolean checkEmailCode(Map<String, Object> map);//校验邮箱验证码
    void register(Map<String, Object> map);//注册
    String login(Map<String, Object> map);//登录
    void resetPassword(Map<String, Object> map);//重置密码
}
