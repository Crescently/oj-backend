package com.cre.ojcodesandbox.controller;

import com.cre.ojcodesandbox.CodeSandbox;
import com.cre.ojcodesandbox.model.ExecuteCodeRequest;
import com.cre.ojcodesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController("/")
public class MainController {
    @Resource
    private CodeSandboxFactory codeSandboxFactory;

    /**
     * 执行代码
     */
    @PostMapping("executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        String language = executeCodeRequest.getLanguage();
        // 根据语言类型选择对应的沙箱实例
        CodeSandbox sandbox = codeSandboxFactory.getSandbox(language);
        ExecuteCodeResponse executeCodeResponse = sandbox.executeCode(executeCodeRequest);
        log.info("executeCodeResponse: {}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
