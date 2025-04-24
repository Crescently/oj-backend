package com.cre.oj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.mapper.CommentMapper;
import com.cre.oj.mapper.UserMapper;
import com.cre.oj.model.entity.Comment;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.comment.CommentAddRequest;
import com.cre.oj.model.request.comment.CommentQueryRequest;
import com.cre.oj.model.vo.CommentVO;
import com.cre.oj.service.CommentService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Override
    public void addComment(CommentAddRequest commentAddRequest, long userId) {
        String content = commentAddRequest.getContent();
        Long questionId = commentAddRequest.getQuestionId();
        Comment comment = Comment.builder().questionId(questionId).content(content).userId(userId).build();
        commentMapper.insert(comment);
    }

    @Override
    public List<CommentVO> listCommentById(CommentQueryRequest commentQueryRequest) {
        Long questionId = commentQueryRequest.getQuestionId();
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id", questionId);

        List<CommentVO> commentVOList = new ArrayList<>();

        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        for (Comment comment : commentList) {
            User user = userMapper.selectById(comment.getUserId());
            Long id = comment.getId();
            String content = comment.getContent();
            LocalDateTime createTime = comment.getCreateTime();

            CommentVO commentVO = CommentVO.builder().id(id).content(content).userVO(userService.getUserVO(user)).createTime(createTime).build();
            commentVOList.add(commentVO);
        }

        return commentVOList;
    }
}
