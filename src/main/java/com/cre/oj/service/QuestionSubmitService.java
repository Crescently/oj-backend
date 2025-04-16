package com.cre.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.questionsubmit.QuestionSubmitAddRequest;

/**
* @author Crescentlymon
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-04-15 16:39:02
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
