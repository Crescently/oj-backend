package com.cre.oj.model.request.question;

import lombok.Data;

@Data
public class JudgeConfig {
    /**
     * 时间限制
     */
    private Long timeLimit;
    /**
     * 内存限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
}

