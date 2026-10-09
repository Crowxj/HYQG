package com.kyhorizon.hyqgsbs.service;

import java.util.Map;

public interface AuthService {
    void sendEmailCode(Map<String, Object> map);
    boolean checkEmailCode(Map<String, Object> map);
    void register(Map<String, Object> map);
    String login(Map<String, Object> map);
}
