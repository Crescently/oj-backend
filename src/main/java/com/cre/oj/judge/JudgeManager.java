package com.cre.oj.judge;

import com.cre.oj.judge.strategy.DefaultJudgeStrategy;
import com.cre.oj.judge.strategy.JavaLanguageJudgeStrategy;
import com.cre.oj.judge.strategy.JudgeContext;
import com.cre.oj.judge.strategy.JudgeStrategy;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.request.questionsubmit.JudgeInfo;
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
        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        if("java".equals(language)){
            judgeStrategy=new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}