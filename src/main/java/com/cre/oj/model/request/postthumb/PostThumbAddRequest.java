package com.cre.oj.model.request.postthumb;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 帖子点赞请求
 */
@Data
public class PostThumbAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 帖子 id
     */
    private Long postId;
}