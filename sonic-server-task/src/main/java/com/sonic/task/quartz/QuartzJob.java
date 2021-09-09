package com.sonic.task.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sonic.task.models.Jobs;
import com.sonic.task.models.interfaces.JobType;
import com.sonic.task.service.JobsService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des 任务实现类
 * @date 2021/8/21 17:44
 */
@Component
public class QuartzJob extends QuartzJobBean implements Job {
    private final Logger logger = LoggerFactory.getLogger(QuartzJob.class);
    @Autowired
    private JobsService jobsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Jobs jobs = jobsService.findById(dataMap.getInt("id"));
        if (jobs != null) {
            JSONObject data = JSON.parseObject(jobs.getContent());
            switch (jobs.getType()) {
                case JobType.TEST_JOB:
                    int suiteId = data.getInteger("suiteId");
                case JobType.CLEAN_FILE_JOB:
                case JobType.CLEAN_RESULT_JOB:
                    break;
            }
        }
    }
}
