package com.cre.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cre.oj.common.BaseResponse;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.model.entity.PostComment;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.postcomment.PostCommentAddRequest;
import com.cre.oj.model.request.postcomment.PostCommentQueryRequest;
import com.cre.oj.model.vo.PostCommentVO;
import com.cre.oj.service.PostCommentService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 帖子评论接口
 */
@RestController
@RequestMapping("/post_comment")
@Slf4j
public class PostCommentController {

    @Resource
    private PostCommentService postCommentService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse addPostComment(@RequestBody PostCommentAddRequest postCommentAddRequest, HttpServletRequest request) {
        if (postCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postCommentService.addPostComment(postCommentAddRequest, loginUser.getId());
        return BaseResponse.success();
    }


    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PostCommentVO>> listPostCommentVOByPage(@RequestBody PostCommentQueryRequest postCommentQueryRequest, HttpServletRequest request) {
        long current = postCommentQueryRequest.getCurrent();
        long size = postCommentQueryRequest.getPageSize();
        Page<PostComment> commentPage = postCommentService.page(new Page<>(current, size), postCommentService.getQueryWrapper(postCommentQueryRequest));
        return BaseResponse.success(postCommentService.getPostCommentVOPage(commentPage, request));
    }
}
