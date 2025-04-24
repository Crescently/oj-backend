package com.cre.oj.judge.strategy;

import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.request.question.JudgeCase;
import com.cre.oj.judge.codesandbox.model.JudgeInfo;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase>judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;
}