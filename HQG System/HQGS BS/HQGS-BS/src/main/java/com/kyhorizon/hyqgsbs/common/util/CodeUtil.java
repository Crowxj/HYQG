package com.kyhorizon.hyqgsbs.common.util;

import java.util.Random;

/**
 * 验证码工具类
 */
public class CodeUtil {
    /**
     * 生成6位数字验证码
     * @return 6位数字字符串
     */
    public static String generateSixDigitCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
