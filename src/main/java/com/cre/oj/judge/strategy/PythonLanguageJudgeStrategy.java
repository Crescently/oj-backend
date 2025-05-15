package com.cre.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.cre.oj.judge.codesandbox.model.JudgeInfo;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.enums.JudgeInfoMessageEnum;
import com.cre.oj.model.request.question.JudgeCase;
import com.cre.oj.model.request.question.JudgeConfig;
import com.cre.oj.utils.StringUtil;

import java.util.List;

/**
 * 默认判题策略
 */
public class PythonLanguageJudgeStrategy implements JudgeStrategy {
    /**
     * 执行判题
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPT;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        if (memory != null) {
            judgeInfoResponse.setMemory(memory / 1024L);
        }
        judgeInfoResponse.setTime(time);
        //先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        //判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(StringUtil.removeInvisibleCharacters(outputList.get(i)))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory == null) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_EXCEPTION;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        if (((memory / 1024L) / 3) > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        long PYTHON_PROGRAM_TIME_COST = 1500L;
        if (time == null) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        if ((time - PYTHON_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
        return judgeInfoResponse;
    }
}