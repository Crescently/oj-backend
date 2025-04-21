package com.cre.oj.judge.codesandbox.impl;

import com.cre.oj.judge.codesandbox.CodeSandbox;
import com.cre.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cre.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cre.oj.model.enums.JudgeInfoMessageEnum;
import com.cre.oj.model.enums.QuestionSubmitStatusEnum;
import com.cre.oj.model.request.questionsubmit.JudgeInfo;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(executeCodeRequest.getInputList());
        executeCodeResponse.setMessage("Success");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPT.getValue());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(400L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
