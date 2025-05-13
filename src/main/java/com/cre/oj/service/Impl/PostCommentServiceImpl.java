package com.cre.oj.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.constant.CommonConstant;
import com.cre.oj.mapper.PostCommentMapper;
import com.cre.oj.model.entity.PostComment;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.request.postcomment.PostCommentAddRequest;
import com.cre.oj.model.request.postcomment.PostCommentQueryRequest;
import com.cre.oj.model.vo.PostCommentVO;
import com.cre.oj.service.PostCommentService;
import com.cre.oj.service.UserService;
import com.cre.oj.utils.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {
    @Resource
    private PostCommentMapper postCommentMapper;

    @Resource
    private UserService userService;


    @Override
    public void addPostComment(PostCommentAddRequest postCommentAddRequest, long userId) {
        String content = postCommentAddRequest.getContent();
        Long postId = postCommentAddRequest.getPostId();
        PostComment postComment = new PostComment();
        postComment.setContent(content);
        postComment.setUserId(userId);
        postComment.setPostId(postId);
        postCommentMapper.insert(postComment);
    }


    @Override
    public Wrapper<PostComment> getQueryWrapper(PostCommentQueryRequest postCommentQueryRequest) {
        QueryWrapper<PostComment> queryWrapper = new QueryWrapper<>();
        if (postCommentQueryRequest == null) {
            return queryWrapper;
        }
        Long postId = postCommentQueryRequest.getPostId();
        String sortField = postCommentQueryRequest.getSortField();
        String sortOrder = postCommentQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(postId), "post_id", postId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public Page<PostCommentVO> getPostCommentVOPage(Page<PostComment> commentPage, HttpServletRequest request) {
        List<PostComment> commentList = commentPage.getRecords();
        Page<PostCommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollUtil.isEmpty(commentList)) {
            return commentVOPage;
        }

        Set<Long> userIdSet = commentList.stream().map(PostComment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));

        // 3. 填充数据
        List<PostCommentVO> commentVOList = commentList.stream().map(comment -> {
            Long userId = comment.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            return PostCommentVO.builder().id(comment.getId()).content(comment.getContent()).userVO(userService.getUserVO(user)).createTime(comment.getCreateTime()).build();
        }).collect(Collectors.toList());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

}
