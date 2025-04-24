package com.cre.oj.model.request.comment;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class CommentAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String content;

    private Long questionId;
}
