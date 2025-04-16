package com.cre.oj.utils;

import java.util.Map;

public class LoginUserInfoUtil {
    public static Long getUserId() {
        // 获取当前登录用户的id
        Map<String, Object> map = ThreadLocalUtil.get();
        return (Long) map.get("id");
    }

    public static String getAccount() {
        Map<String, Object> map = ThreadLocalUtil.get();
        return (String) map.get("userAccount");
    }
}
