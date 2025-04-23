package com.cre.oj.controller;


import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.questionthumb.QuestionThumbAddRequest;
import com.cre.oj.service.QuestionThumbService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/question_thumb")
@Slf4j
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionThumbAddRequest questionThumbAddRequest
     * @param request request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest questionThumbAddRequest, HttpServletRequest request) {
        if (questionThumbAddRequest == null || questionThumbAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long questionId = questionThumbAddRequest.getQuestionId();
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return BaseResponse.success(result);
    }

}
