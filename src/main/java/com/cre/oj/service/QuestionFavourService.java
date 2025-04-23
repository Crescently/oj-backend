package com.cre.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionFavour;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;


public interface QuestionFavourService extends IService<QuestionFavour> {


    int doQuestionFavour(Long questionId, User loginUser);


    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
                                            Long favourUserId);


    int doQuestionFavourInner(Long userId, Long questionId);

}
