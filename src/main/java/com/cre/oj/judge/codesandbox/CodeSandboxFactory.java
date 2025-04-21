package com.cre.oj.judge.codesandbox;

import com.cre.oj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.cre.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cre.oj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建对应的沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 创建沙箱实例
     *
     * @param type 沙箱类型
     * @return 沙箱实例
     */
    public static CodeSandbox newInstance(String type) {
        return switch (type) {
            case "example" -> new ExampleCodeSandbox();
            case "remote" -> new RemoteCodeSandbox();
            case "thirdParty" -> new ThirdPartyCodeSandbox();
            default -> new ExampleCodeSandbox();
        };
    }

}
