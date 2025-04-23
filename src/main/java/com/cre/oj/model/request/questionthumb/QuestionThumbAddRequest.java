package com.cre.oj.model.request.questionthumb;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QuestionThumbAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private Long questionId;
}