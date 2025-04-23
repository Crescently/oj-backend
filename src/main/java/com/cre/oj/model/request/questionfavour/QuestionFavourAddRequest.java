package com.cre.oj.model.request.questionfavour;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class QuestionFavourAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private Long questionId;
}