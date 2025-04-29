package com.cre.oj.model.request.comment;

import com.cre.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long questionId;
}
