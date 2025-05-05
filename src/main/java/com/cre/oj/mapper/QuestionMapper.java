package com.cre.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cre.oj.model.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Crescentlymon
 */
public interface QuestionMapper extends BaseMapper<Question> {
    @Update("UPDATE question SET submit_num = #{submitNum}, accepted_num = #{acceptedNum}, update_time = NOW() " +
            "WHERE id = #{questionId} AND isDelete = 0")
    void updateSubmitAndAcceptedNum(@Param("questionId") Long questionId,
                                    @Param("submitNum") Integer submitNum,
                                    @Param("acceptedNum") Integer acceptedNum);
}




