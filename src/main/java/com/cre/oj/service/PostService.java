package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Post;
import com.cre.oj.model.request.post.PostQueryRequest;
import com.cre.oj.model.vo.PostVO;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 帖子服务
 */
public interface PostService extends IService<Post> {

    /**
     * 校验
     */
    void validPost(Post post, boolean add);

    /**
     * 获取查询条件
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);


    /**
     * 获取帖子封装
     */
    PostVO getPostVO(Post post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request);

    boolean incrementViewCountOnce(Long postId, Long userId);
}
