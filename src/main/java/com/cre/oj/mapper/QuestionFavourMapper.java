package com.cre.oj.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionFavour;
import org.apache.ibatis.annotations.Param;


public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {

    Page<Question> listFavourQuestionByPage(IPage<Question> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper,
                                            Long favourUserId);

}




