package com.cre.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cre.oj.model.entity.PostComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 帖子评论数据库操作
 */
public interface PostCommentMapper extends BaseMapper<PostComment> {

    List<PostComment> selectByPostIds(@Param("postIds") Set<Long> postIds);

}
