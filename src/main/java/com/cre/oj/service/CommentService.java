package com.cre.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Comment;
import com.cre.oj.model.request.comment.CommentAddRequest;
import com.cre.oj.model.request.comment.CommentQueryRequest;
import com.cre.oj.model.vo.CommentVO;

import java.util.List;


public interface CommentService extends IService<Comment> {
    void addComment(CommentAddRequest commentAddRequest,long userId);

    List<CommentVO> listCommentById(CommentQueryRequest commentQueryRequest);
}
