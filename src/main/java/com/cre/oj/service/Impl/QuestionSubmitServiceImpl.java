package com.cre.oj.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.mapper.QuestionSubmitMapper;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.enums.QuestionSubmitLanguageEnum;
import com.cre.oj.model.enums.QuestionSubmitStatusEnum;
import com.cre.oj.model.request.questionsubmit.QuestionSubmitAddRequest;
import com.cre.oj.service.QuestionService;
import com.cre.oj.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Crescentlymon
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2025-04-15 16:39:02
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据输入失败");
        }
        return questionSubmit.getId();
    }
}




