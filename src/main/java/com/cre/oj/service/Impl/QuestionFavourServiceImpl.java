package com.cre.oj.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.mapper.QuestionFavourMapper;
import com.cre.oj.mapper.QuestionThumbMapper;
import com.cre.oj.model.entity.Question;
import com.cre.oj.model.entity.QuestionFavour;
import com.cre.oj.model.entity.QuestionThumb;
import com.cre.oj.model.entity.User;
import com.cre.oj.model.vo.QuestionVO;
import com.cre.oj.model.vo.UserVO;
import com.cre.oj.service.QuestionFavourService;
import com.cre.oj.service.QuestionService;
import com.cre.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour> implements QuestionFavourService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionFavourMapper questionFavourMapper;

    @Resource
    private QuestionThumbMapper questionThumbMapper;


    /**
     * 帖子收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    @Override
    public int doQuestionFavour(Long questionId, User loginUser) {
        // 判断是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已帖子收藏
        Long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        QuestionFavourService questionFavourService = (QuestionFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionFavourService.doQuestionFavourInner(userId, questionId);
        }
    }

    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, Long favourUserId) {
        if (favourUserId == null) {
            return new Page<>();
        }
        return baseMapper.listFavourQuestionByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(Long userId, Long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);
        QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>(questionFavour);
        QuestionFavour oldQuestionFavour = this.getOne(questionFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldQuestionFavour != null) {
            result = this.remove(questionFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = questionService.update().eq("id", questionId).gt("favour_num", 0).setSql("favour_num = favour_num - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(questionFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = questionService.update().eq("id", questionId).setSql("favour_num = favour_num + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }


}




