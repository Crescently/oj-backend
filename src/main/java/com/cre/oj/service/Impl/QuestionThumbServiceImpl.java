package com.cre.oj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.mapper.QuestionThumbMapper;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionThumb;
import com.cre.oj.model.entity.User;
import com.cre.oj.service.QuestionService;
import com.cre.oj.service.QuestionThumbService;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 帖子点赞服务实现
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb> implements QuestionThumbService {

    @Resource
    private QuestionService questionService;

    @Override
    public int doQuestionThumb(Long questionId, User loginUser) {
        // 判断文章是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        Long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionThumbService questionThumbService = (QuestionThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionThumbService.doQuestionThumbInner(userId, questionId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionThumbInner(Long userId, Long questionId) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        QueryWrapper<QuestionThumb> thumbQueryWrapper = new QueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update().eq("id", questionId).gt("thumb_num", 0).setSql("thumb_num = thumb_num - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionThumb);
            if (result) {
                // 点赞数 + 1
                result = questionService.update().eq("id", questionId).setSql("thumb_num = thumb_num + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




