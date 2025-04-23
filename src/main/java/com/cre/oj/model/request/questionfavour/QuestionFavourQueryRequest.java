package com.cre.oj.model.request.questionfavour;

import com.cre.oj.common.PageRequest;
import com.cre.oj.model.request.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 文章查询请求
     */
    private QuestionQueryRequest questionQueryRequest;
    /**
     * 用户 id
     */
    private Long userId;
}