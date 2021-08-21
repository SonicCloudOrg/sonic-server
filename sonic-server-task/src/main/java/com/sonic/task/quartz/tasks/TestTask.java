package com.sonic.task.quartz.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des 任务实现类
 * @date 2021/8/21 17:44
 */
@Component
public class TestTask extends QuartzJobBean implements Job {
    private final Logger logger = LoggerFactory.getLogger(TestTask.class);
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
