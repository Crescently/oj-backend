package com.cre.oj.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.oj.common.ErrorCode;
import com.cre.oj.constant.CommonConstant;
import com.cre.oj.exception.BusinessException;
import com.cre.oj.exception.ThrowUtils;
import com.cre.oj.mapper.QuestionFavourMapper;
import com.cre.oj.mapper.QuestionMapper;
import com.cre.oj.mapper.QuestionSubmitMapper;
import com.cre.oj.mapper.QuestionThumbMapper;
import com.cre.oj.model.dto.QuestionStatDTO;
import com.cre.oj.model.entity.*;
import com.cre.oj.model.request.question.QuestionQueryRequest;
import com.cre.oj.model.vo.QuestionVO;
import com.cre.oj.model.vo.UserVO;
import com.cre.oj.service.QuestionService;
import com.cre.oj.service.UserService;
import com.cre.oj.utils.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Crescentlymon
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionFavourMapper questionFavourMapper;

    @Resource
    private QuestionThumbMapper questionThumbMapper;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 校验题目合法
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }


    @Override
    public QuestionVO getQuestionVO(Question question) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);

        //2. 根据文章id查询当前用户是否收藏
        QueryWrapper<QuestionFavour> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id", questionId);
        List<QuestionFavour> questionFavours = questionFavourMapper.selectList(queryWrapper);
        if (questionFavours.isEmpty()) {
            questionVO.setFavour(false);
        } else {
            for (QuestionFavour questionFavour : questionFavours) {
                questionVO.setFavour(questionFavour.getUserId().equals(userId));
            }
        }
        // 3. 根据文章id查询当前用户是否点赞
        QueryWrapper<QuestionThumb> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("question_id", questionId);
        List<QuestionThumb> questionThumbs = questionThumbMapper.selectList(queryWrapper2);
        if (questionThumbs.isEmpty()) {
            questionVO.setThumb(false);
        } else {
            for (QuestionThumb questionThumb : questionThumbs) {
                questionVO.setThumb(questionThumb.getUserId().equals(userId));
            }
        }

        return questionVO;
    }

    public Page<QuestionVO> getHistoryQuestionVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionVOPage;
        }
        // 1. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmitList.stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
        List<Question> questionList = this.listByIds(questionIdSet);
        // 2. 获取每到题目对应的创建者的用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        // 3. 填充数据
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }


    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));

        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUser(request);

        if (loginUser != null) {
            Set<Long> questionIdSet = questionList.stream().map(Question::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞状态
            List<QuestionThumb> questionThumbList = questionThumbMapper.selectList(new QueryWrapper<QuestionThumb>().in("question_id", questionIdSet).eq("user_id", loginUser.getId()));
            questionThumbList.forEach(questionThumb -> questionIdHasThumbMap.put(questionThumb.getQuestionId(), true));

            // 获取收藏状态
            List<QuestionFavour> questionFavourList = questionFavourMapper.selectList(new QueryWrapper<QuestionFavour>().in("question_id", questionIdSet).eq("user_id", loginUser.getId()));
            questionFavourList.forEach(questionFavour -> questionIdHasFavourMap.put(questionFavour.getQuestionId(), true));

        }

        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Transactional
    public void updateSubmitAndAcceptedNum() {
        List<QuestionStatDTO> submitStats = questionSubmitMapper.countTotalSubmissions();
        List<QuestionStatDTO> acceptedStats = questionSubmitMapper.countAcceptedSubmissions();

        Map<Long, Integer> acceptedMap = acceptedStats.stream().collect(Collectors.toMap(QuestionStatDTO::getQuestionId, QuestionStatDTO::getCount));

        for (QuestionStatDTO submitStat : submitStats) {
            Long questionId = submitStat.getQuestionId();
            Integer submitCount = submitStat.getCount();
            Integer acceptedCount = acceptedMap.getOrDefault(questionId, 0);

            questionMapper.updateSubmitAndAcceptedNum(questionId, submitCount, acceptedCount);
        }
    }

    @Override
    public String getQuestionAnswerById(Long questionId, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        // 根据id查看该是否用户提交过
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        log.info("问题ID：{}", questionId);
        queryWrapper.eq("question_id", questionId);
        queryWrapper.eq("user_id", user.getId());
        Long count = questionSubmitMapper.selectCount(queryWrapper);
        // 若用户提交过或用户是管理员，则获取问题答案
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您未作答过该题目");
        }
        if (!userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Question question = questionMapper.selectById(questionId);
        return question.getAnswer();
    }
}




