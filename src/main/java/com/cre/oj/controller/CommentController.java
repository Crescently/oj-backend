package com.cre.oj.controller;

import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.comment.CommentAddRequest;
import com.cre.oj.model.request.comment.CommentQueryRequest;
import com.cre.oj.model.vo.CommentVO;
import com.cre.oj.service.CommentService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        commentService.addComment(commentAddRequest, loginUser.getId());
        return BaseResponse.success();
    }


    @PostMapping("/list")
    public BaseResponse<List<CommentVO>> listCommentById(@RequestBody CommentQueryRequest commentQueryRequest) {
        if (commentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CommentVO> commentList = commentService.listCommentById(commentQueryRequest);
        return BaseResponse.success(commentList);
    }
}
