package com.cre.oj.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private UserVO userVO;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
