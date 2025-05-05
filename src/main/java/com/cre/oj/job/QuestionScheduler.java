package com.cre.oj.job;


import com.cre.oj.service.QuestionService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class QuestionScheduler {

    @Resource
    private QuestionService questionService;

    // 每10min执行一次
    @Scheduled(cron = "0 */10 * * * ?")
    public void updateQuestionStats() {
        questionService.updateSubmitAndAcceptedNum();
    }
}
