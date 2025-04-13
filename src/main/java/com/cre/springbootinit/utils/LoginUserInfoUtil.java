package com.cre.springbootinit.utils;

import java.util.Map;

public class LoginUserInfoUtil {
    public static Long getUserId() {
        // 获取当前登录用户的id
        Map<String, Object> map = ThreadLocalUtil.get();
        return (Long) map.get("id");
    }
}
