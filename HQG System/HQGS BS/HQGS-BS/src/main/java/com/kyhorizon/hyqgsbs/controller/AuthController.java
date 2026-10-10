package com.kyhorizon.hyqgsbs.controller;

import com.kyhorizon.hyqgsbs.common.Result;
import com.kyhorizon.hyqgsbs.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 发送邮箱验证码
     * 前端传 {username:"test", type:"register"} 注册
     * 前端传 {username:"test", type:"resetPwd"} 重置密码
     */
    @PostMapping("/sendEmailCode")
    public Result<Object> sendEmailCode(@RequestBody Map<String,Object> map){
        authService.sendEmailCode(map);
        return Result.successMsg("验证码发送成功");
    }

    /**
     * 校验邮箱验证码（下一步接口）
     * 前端传 {username:"test", code:"123456", type:"register"}
     */
    @PostMapping("/checkEmailCode")
    public Result<Object> checkEmailCode(@RequestBody Map<String,Object> map){
        boolean ok = authService.checkEmailCode(map);
        if(ok){
            return Result.successMsg("验证码验证成功");
        }else{
            return Result.badRequest("验证码不存在或已过期,请重新输入");
        }
    }

    /**
     * 注册提交
     * 前端传 {username:"test", password:"xxx", code:"123456"}
     */
    @PostMapping("/register")
    public Result<Object> register(@RequestBody Map<String,Object> map){
        authService.register(map);
        return Result.successMsg("注册成功");
    }

    /**
     * 【新增】重置密码提交接口
     * 前端找回密码第二步提交：{username:"test", password:"xxx", code:"123456"}
     */
    @PostMapping("/resetPassword")
    public Result<Object> resetPassword(@RequestBody Map<String,Object> map){
        authService.resetPassword(map);
        return Result.successMsg("密码重置成功");
    }

    /**
     * 登录账号
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody Map<String,Object> map){
        String loginUsername = (String) map.get("username");
        String token = authService.login(map);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("username", loginUsername);
        return Result.success(resultMap,"登录成功");
    }
}
