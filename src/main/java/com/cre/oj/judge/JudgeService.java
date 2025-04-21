package com.cre.oj.judge;


import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
