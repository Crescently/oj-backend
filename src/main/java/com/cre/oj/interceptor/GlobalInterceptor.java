package com.cre.oj.interceptor;


import com.cre.oj.common.ErrorCode;
import com.cre.oj.utils.JwtUtil;
import com.cre.oj.utils.ThreadLocalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
// 全局登录拦截器
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler;
    private Exception ex;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        try {
            // 验证token是否有效，如果无效则返回错误信息
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("没有token，请确认是否登录");
            }
            // 从redis中获取token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken == null) {
                // token 已经失效了
                throw new RuntimeException("token失效了，请重新登录");
            }
            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把claims存入THREAD_LOCAL中，方便后续获取
            ThreadLocalUtil.set(claims);
            // 刷新redis 中的token有效期
            // TODO 可能存在的问题，当用户访问不被拦截的网页时，Redis不会刷新时间
            stringRedisTemplate.expire(token, 1, TimeUnit.HOURS);
            // 放行
            return true;
        } catch (Exception e) {
            log.error("请求失败");
            returnErrorResponse(response, e.getMessage());
            // 不放行
            return false;
        }
    }

    private void returnErrorResponse(HttpServletResponse response, String message) {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        try {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", 401);
            errorDetails.put("code", ErrorCode.NOT_LOGIN_ERROR.getCode());
            errorDetails.put("message", message);
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        } catch (IOException e) {
            log.error("Failed to write error response", e);
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清空threadLocal中的数据 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
