package com.cre.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.QuestionThumb;
import com.cre.oj.model.entity.User;;


public interface QuestionThumbService extends IService<QuestionThumb> {


    int doQuestionThumb(Long questionId, User loginUser);


    int doQuestionThumbInner(Long userId, Long questionId);
}
