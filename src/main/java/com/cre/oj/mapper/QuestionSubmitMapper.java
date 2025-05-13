package com.cre.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cre.oj.model.dto.QuestionStatDTO;
import com.cre.oj.model.entity.QuestionSubmit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Crescentlymon
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {
    @Select("SELECT question_id, COUNT(*) AS count " +
            "FROM question_submit WHERE isDelete = 0 GROUP BY question_id")
    List<QuestionStatDTO> countTotalSubmissions();

    @Select("SELECT question_id, COUNT(*) AS count " +
            "FROM question_submit WHERE isDelete = 0 AND status = 2 GROUP BY question_id")
    List<QuestionStatDTO> countAcceptedSubmissions();

    int countDistinctQuestionsByUserId(@Param("userId") Long userId);
}




