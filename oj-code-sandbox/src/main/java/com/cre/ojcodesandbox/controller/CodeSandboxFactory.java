package com.cre.ojcodesandbox.controller;

import com.cre.ojcodesandbox.CodeSandbox;
import com.cre.ojcodesandbox.cppcodesandbox.CppDockerCodeSandbox;
import com.cre.ojcodesandbox.javasandbox.JavaDockerCodeSandbox;
import com.cre.ojcodesandbox.pythoncodesandbox.PythonDockerCodeSandbox;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CodeSandboxFactory {

    private final Map<String, CodeSandbox> sandboxMap = new HashMap<>();

    public CodeSandboxFactory() {
        sandboxMap.put("java", new JavaDockerCodeSandbox());
        sandboxMap.put("cpp", new CppDockerCodeSandbox());
        sandboxMap.put("python", new PythonDockerCodeSandbox());
    }

    public CodeSandbox getSandbox(String language) {
        CodeSandbox sandbox = sandboxMap.get(language.toLowerCase());
        if (sandbox == null) {
            throw new IllegalArgumentException("不支持的语言类型: " + language);
        }
        return sandbox;
    }
}
