package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.request.question.QuestionQueryRequest;
import com.cre.oj.model.vo.QuestionVO;

/**
* @author Crescentlymon
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-04-15 16:34:38
*/
public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean b);

    QuestionVO getQuestionVO(Question question);

    Wrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage);
}
