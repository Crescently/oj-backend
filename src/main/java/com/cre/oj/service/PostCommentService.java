package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.PostComment;
import com.cre.oj.model.request.postcomment.PostCommentAddRequest;
import com.cre.oj.model.request.postcomment.PostCommentQueryRequest;
import com.cre.oj.model.vo.PostCommentVO;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 帖子评论服务
 */
public interface PostCommentService extends IService<PostComment> {
    void addPostComment(PostCommentAddRequest postCommentAddRequest, long userId);


    Wrapper<PostComment> getQueryWrapper(PostCommentQueryRequest postCommentQueryRequest);

    /**
     * 获取评论对象分页（脱敏）
     */
    Page<PostCommentVO> getPostCommentVOPage(Page<PostComment> commentPage, HttpServletRequest request);

}
