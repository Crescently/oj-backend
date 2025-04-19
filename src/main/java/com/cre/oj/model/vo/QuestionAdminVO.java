package com.cre.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.request.question.JudgeCase;
import com.cre.oj.model.request.question.JudgeConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class QuestionAdminVO {
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题用例（json 数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 对象转包装类
     */
    public static QuestionAdminVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionAdminVO questionAdminVO = new QuestionAdminVO();
        BeanUtils.copyProperties(question, questionAdminVO);
        questionAdminVO.setTags(JSONUtil.toList(question.getTags(), String.class));

        String judgeConfig = question.getJudgeConfig();
        questionAdminVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));

        String judgeCase = question.getJudgeCase();
        questionAdminVO.setJudgeCase(JSONUtil.toList(judgeCase, JudgeCase.class));
        return questionAdminVO;
    }

}
