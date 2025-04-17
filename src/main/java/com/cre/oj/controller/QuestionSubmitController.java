package com.cre.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.exception.ThrowUtils;
import com.cre.oj.model.entity.QuestionSubmit;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.questionsubmit.QuestionSubmitAddRequest;
import com.cre.oj.model.request.questionsubmit.QuestionSubmitQueryRequest;
import com.cre.oj.model.vo.QuestionSubmitVO;
import com.cre.oj.service.QuestionSubmitService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     */
    @PostMapping("/submit")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return BaseResponse.success(questionSubmitId);
    }

    /**
     * 分页获取列表（封装类）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionQueryRequest, HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        final User loginUser = userService.getLoginUser(request);
        Page<QuestionSubmit> questionPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getQueryWrapper(questionQueryRequest));
        return BaseResponse.success(questionSubmitService.getQuestionSubmitVOPage(questionPage, loginUser));
    }

}

