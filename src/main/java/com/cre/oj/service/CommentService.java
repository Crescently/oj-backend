package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Comment;
import com.cre.oj.model.request.comment.CommentAddRequest;
import com.cre.oj.model.request.comment.CommentQueryRequest;
import com.cre.oj.model.vo.CommentVO;
import jakarta.servlet.http.HttpServletRequest;


public interface CommentService extends IService<Comment> {
    void addComment(CommentAddRequest commentAddRequest, long userId);


    Wrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);

    /**
     * 获取评论对象分页（脱敏）
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);
}
