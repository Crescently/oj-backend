package com.cre.ojcodesandbox.controller;

import com.cre.ojcodesandbox.JavaNativeCodeSandbox;
import com.cre.ojcodesandbox.model.ExecuteCodeRequest;
import com.cre.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class MainController {

    //定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @GetMapping("health")
    public String healthCheck() {
        return "OK";
    }

    /**
     * 执行代码
     */
    @PostMapping("executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        // 基本的认证
//        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
//        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
//            response.setStatus(403);
//            return null;
//        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }
}
