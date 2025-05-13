package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.request.question.QuestionQueryRequest;
import com.cre.oj.model.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;

public interface QuestionService extends IService<Question> {
    /**
     * 校验题目合法
     */
    void validQuestion(Question question, boolean b);

    /**
     * 获取问题对象（脱敏）
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 获取查询包装类
     */
    Wrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取问题对象分页（脱敏）
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    Page<QuestionVO> getHistoryQuestionVOPage(Page<QuestionSubmit> questionSubmitPage, long userId);

    void updateSubmitAndAcceptedNum();

    String getQuestionAnswerById(Long id, HttpServletRequest request);
}
