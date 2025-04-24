package com.cre.oj.model.request.comment;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommentQueryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long questionId;
}
