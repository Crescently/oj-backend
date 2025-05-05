package com.cre.oj.judge;

import com.cre.oj.judge.strategy.*;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.judge.codesandbox.model.JudgeInfo;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new JavaLanguageJudgeStrategy();
        if ("cpp".equals(language)) {
            judgeStrategy = new CppLanguageJudgeStrategy();
        }
        if ("python".equals(language)) {
            judgeStrategy = new PythonLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}