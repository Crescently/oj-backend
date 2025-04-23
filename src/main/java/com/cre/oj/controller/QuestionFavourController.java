package com.cre.oj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.question.QuestionQueryRequest;
import com.cre.oj.model.request.questionfavour.QuestionFavourAddRequest;
import com.cre.oj.model.vo.QuestionVO;
import com.cre.oj.service.QuestionFavourService;
import com.cre.oj.service.QuestionService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question_favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    /**
     * 问题收藏
     *
     * @param questionFavourAddRequest questionFavourAddRequest
     * @param request                  request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest questionFavourAddRequest, HttpServletRequest request) {
        if (questionFavourAddRequest == null || questionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long questionId = questionFavourAddRequest.getQuestionId();
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return BaseResponse.success(result);
    }

    /**
     * 获取用户自己的收藏的问题列表
     *
     * @param questionQueryRequest questionQueryRequest
     * @param request              request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest), loginUser.getId());
        return BaseResponse.success(questionService.getQuestionVOPage(questionPage, request));
    }

}
